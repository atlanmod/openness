/* pr_commit_issue_info_per_project collects all the information related to pull requests, issues, etc. for all the projects in the data-set */
drop table if exists pr_commit_issue_info_per_project;
create table pr_commit_issue_info_per_project (
					project_id int(11),
					project_name varchar(255),
					pull_request_id int(11),
					commit_id int(11),
					pull_requester int(11),
					merged int(1),
					intra_branch int(1),
					base_repo_id int(11),
					head_repo_id int(11),
					author_commit int(11),
					committer_id int(11),
					commit_time timestamp,
					pull_request_action varchar(255),
					pull_request_action_time timestamp,
					issue_id int(11),
					reporter_id int(11),
					assignee_id int(11),
					issue_creation timestamp,
					issue_event_actor int(11),
					issue_action varchar(255),
					issue_action_time timestamp
				);
insert into pr_commit_issue_info_per_project (
					project_id,
					project_name,
					pull_request_id,
					commit_id,
					pull_requester,
					merged,
					intra_branch,
					base_repo_id,
					head_repo_id,
					author_commit,
					committer_id,
					commit_time,
					pull_request_action,
					pull_request_action_time,
					issue_id,
					reporter_id,
					assignee_id,
					issue_creation,
					issue_event_actor,
					issue_action,
					issue_action_time
				)
select x.*
from
	projects p
left join
	(select 
			e.*, ie.actor_id as issue_event_actor, ie.action as issue_action, ie.created_at as issue_action_time
	from
		(select d.*, i.id as issue_id, i.reporter_id, i.assignee_id, i.created_at as issue_creation
		from
			(select c.*, prh.action as pull_request_action, prh.created_at as pull_request_action_time
			from
			(select a.*, c.author_id as author_commit, c.committer_id as committer_id, c.created_at as commit_time
			from (
				select pcr.*, pr.user_id as pull_requester, pr.merged, pr.intra_branch, pr.base_repo_id, pr.head_repo_id
				from
					pull_requests pr
				right join
					(select p.id as project_id, p.name as project_name, prc.pull_request_id, prc.commit_id
					from project_commits pc, pull_request_commits prc, projects p
					where pc.project_id = p.id and pc.commit_id = prc.commit_id and p.forked_from is null) as pcr
				on pcr.pull_request_id = pr.id) as a
			left join
				commits c
			on c.id = a.commit_id and c.project_id = a.project_id) as c
			left join
			pull_request_history prh
			on c.pull_request_id = prh.pull_request_id
		) as d
		left join
			issues i
		on d.pull_request_id = i.pull_request_id) as e
	left join
	issue_events ie
	on e.issue_id = ie.issue_id) as x
on p.id = x.project_id
where p.forked_from is null;

/* pm_and_contributors_per_project contains the project members, owners and contributors (who has write permission on the repository) for each project in the data-set */
drop table if exists pm_and_contributors_per_project;
create table pm_and_contributors_per_project (
					project_id int(11),
					project_name varchar(255),
					user_id varchar(255),
					user_name varchar(255),
					user_login varchar(255),
					user_mail varchar(255),
					first_action timestamp,
					type_user varchar(255)
				);
insert into pm_and_contributors_per_project (
					project_id,
					project_name,
					user_id,
					user_name,
					user_login,
					user_mail,
					first_action,
					type_user
				)
select z.*
from projects p
left join
	(select distinct p.id as project_id, p.name as project_name, u.id as user_id, u.name as user_name, u.login as user_login, u.email as user_mail, pm.created_at as first_time, 'project_member' as type_user
	from project_members pm, users u, projects p
	where pm.repo_id = p.id and u.id = pm.user_id
	union
	select p.id as project_id, p.name as project_name, u.id as user_id, u.name as user_name, u.login as user_login, u.email as user_mail, p.created_at as first_time, 'project_owner' as type_user
	from users u, projects p
	where u.id = p.owner_id
	union
	select distinct x.project_id, x.project_name, x.user_id, x.user_name, x.user_login, x.user_mail, min(x.first_time) as first_time, x.type_user
	from
		(select t.project_id, t.project_name, u.id as user_id, u.name as user_name, u.login as user_login, u.email as user_mail, t.pull_request_action_time as first_time, 'internal_contributor' as type_user
		from users u,
			pr_commit_issue_info_per_project t
		where u.id = t.pull_requester and t.intra_branch = 1 and t.pull_requester not in (select pm.user_id 
																						from project_members pm
																						where pm.repo_id = t.project_id
																						union
																						select p.owner_id
																						from projects p
																						where p.id = t.project_id)
		union
		select t.project_id, t.project_name, u.id as user_id, u.name as user_name, u.login as user_login, u.email as user_mail, t.commit_time as first_time, 'internal_contributor' as type_user
		from users u,
			pr_commit_issue_info_per_project t
		where u.id = t.committer_id and t.intra_branch = 1 and t.committer_id not in (select pm.user_id 
																					from project_members pm
																					where pm.repo_id = t.project_id
																					union
																					select p.owner_id
																					from projects p
																					where p.id = t.project_id)
		union
		select distinct t.project_id, t.project_name, u.id as user_id, u.name as user_name, u.login as user_login, u.email as user_mail, t.issue_action_time as first_time, 'internal_contributor' as type_user
		from users u,
			pr_commit_issue_info_per_project t
		where u.id = t.issue_event_actor and t.merged = 0 and t.intra_branch = 0 and t.issue_action in ('closed') and t.issue_event_actor not in (select pm.user_id 
																																										from project_members pm
																																										where pm.repo_id = t.project_id
																																										union
																																										select p.owner_id
																																										from projects p
																																										where p.id = t.project_id)
		union
		select distinct t.project_id, t.project_name, u.id as user_id, u.name as user_name, u.login as user_login, u.email as user_mail, t.issue_action_time as first_time, 'internal_contributor' as type_user
		from users u,
			pr_commit_issue_info_per_project t
		where u.id = t.issue_event_actor and t.merged = 1 and t.intra_branch = 0 and t.issue_action in ('merged') and t.issue_event_actor not in (select pm.user_id 
																																					from project_members pm
																																					where pm.repo_id = t.project_id
																																					union
																																					select p.owner_id
																																					from projects p
																																					where p.id = t.project_id)
		union
		select distinct t.project_id, t.project_name, u.id as user_id, u.name as user_name, u.login as user_login, u.email as user_mail, t.issue_action_time as first_time, 'internal_contributor' as type_user
		from users u,
			pr_commit_issue_info_per_project t
		where u.id = t.issue_event_actor and t.intra_branch = 1 and t.issue_action in ('closed', 'reopened', 'merged') and t.issue_event_actor not in (select pm.user_id 
																																						from project_members pm
																																						where pm.repo_id = t.project_id
																																						union
																																						select p.owner_id
																																						from projects p
																																						where p.id = t.project_id)
		) as x
		where x.user_id not in (select pm.user_id 
								from project_members pm
								where pm.repo_id = x.project_id
								union
								select p.owner_id
								from projects p
								where p.id = x.project_id)
		group by x.project_id, x.user_id) as z
on p.id = z.project_id
where p.forked_from is null;

/* external_contributors_per_project contains the external developers for each project in the data-set that got at least one pull request accepted. Such external developers do not write permission on the repository */
drop table if exists external_contributors_per_project;
create table external_contributors_per_project (
					project_id int(11),
					project_name varchar(255),
					user_id varchar(255),
					user_name varchar(255),
					user_login varchar(255),
					user_mail varchar(255)
				);
insert into external_contributors_per_project (
					project_id,
					project_name,
					user_id,
					user_name,
					user_login,
					user_mail
				)
select x.*
from
	projects p
left join
	(select distinct t.project_id, t.project_name, u.id as user_id, u.name as user_name, u.login as user_login, u.email as user_mail
	from
		users u,
		pr_commit_issue_info_per_project t
	where u.id = t.pull_requester and t.merged = 1 and  t.pull_requester not in (select a.user_id
																				from pm_and_contributors_per_project a
																				where a.project_id = t.project_id
																				)) as x
on p.id = x.project_id
where p.forked_from is null;

/* external_failed_contributors_per_project contains the external developers for each project in the data-set that make pull requests, but never got them accepted. Such external developers do not write permission on the repository */
drop table if exists external_failed_contributors_per_project;
create table external_failed_contributors_per_project (
					project_id int(11),
					project_name varchar(255),
					user_id varchar(255),
					user_name varchar(255),
					user_login varchar(255),
					user_mail varchar(255)
				);
insert into external_failed_contributors_per_project (
					project_id,
					project_name,
					user_id,
					user_name,
					user_login,
					user_mail
				)
select x.*
from
	projects p
left join
	(select distinct t.project_id, t.project_name, u.id as user_id, u.name as user_name, u.login as user_login, u.email as user_mail
	from
		users u,
		pr_commit_issue_info_per_project t
	where u.id = t.pull_requester and t.merged = 0 and  t.pull_requester not in (select a.user_id
																				from pm_and_contributors_per_project a
																				where a.project_id = t.project_id
																				union
																				select a.user_id
																				from external_contributors_per_project a
																				where a.project_id = t.project_id)) as x
on p.id = x.project_id
where p.forked_from is null;

/* first_user_activity_per_project contains the first activity of each developer (project members, collaborators, external contributors, external members) for each project in the data-set */
drop table if exists first_user_activity_per_project;
create table first_user_activity_per_project (
					project_id int(11),
					project_name varchar(255),
					user_id int(11),
					first_activity timestamp
				);
insert into first_user_activity_per_project (
					project_id,
					project_name,
					user_id,
					first_activity
				)
select x.*
from projects p
left join
(select u.project_id, u.project_name, u.actor as user_id, min(u.time_event) as first_activity
from (
	select p.id as project_id, p.name as project_name, ie.actor_id as actor, ie.created_at as time_event
	from issues i, issue_events ie, projects p
	where i.id = ie.issue_id and i.repo_id = p.id
	union
	select p.id as project_id, p.name as project_name, p.owner_id as actor, p.created_at as time_event
	from projects p
	union
	select p.id as project_id, p.name as project_name, ic.user_id as actor, ic.created_at as time_event
	from issue_comments ic, issues i, projects p
	where i.id = ic.issue_id and i.repo_id = p.id and ic.user_id
	union
	select p.id as project_id, p.name as project_name, cc.user_id as actor, cc.created_at as time_event
	from commit_comments cc, commits c, projects p
	where cc.commit_id = c.id and c.project_id = p.id
	union
	select p.id as project_id, p.name as project_name, cc.user_id as actor, cc.created_at as time_event
	from commit_comments cc, project_commits pc, projects p
	where pc.project_id = p.id and cc.commit_id = pc.commit_id
	union
	select p.id as project_id, p.name as project_name, prc.user_id as actor, prc.created_at as time_event
	from pull_requests pr, pull_request_comments prc, projects p
	where pr.id = prc.pull_request_id and pr.base_repo_id = p.id
	union
	select p.id as project_id, p.name as project_name, c.author_id as actor, c.created_at as time_event
	from commits c, projects p
	where c.project_id = p.id
	union
	select p.id as project_id, p.name as project_name, c.author_id as actor, c.created_at as time_event
	from project_commits pc, commits c, projects p
	where pc.project_id = p.id and pc.commit_id = c.id
	union
	select p.id as project_id, p.name as project_name, c.committer_id as actor, c.created_at as time_event
	from commits c, projects p
	where c.project_id = p.id
	union
	select p.id as project_id, p.name as project_name, c.committer_id as actor, c.created_at as time_event
	from project_commits pc, commits c, projects p
	where pc.project_id = p.id and pc.commit_id = c.id
	union
	select p.id as project_id, p.name as project_name, pr.user_id as actor, prh.created_at as time_event
	from pull_requests pr, pull_request_history prh, projects p
	where pr.id = prh.pull_request_id and pr.base_repo_id = p.id
	union
	select p.id as project_id, p.name as project_name, i.reporter_id as actor, i.created_at as time_event
	from issues i, projects p
	where i.repo_id = p.id
	union
	select p.id as project_id, p.name as project_name, i.assignee_id as actor, i.created_at as time_event
	from issues i, projects p
	where i.repo_id = p.id
	union
	select p1.id as project_id, p1.name as project_name, p2.owner_id as actor, p2.created_at as time_event
	from projects p1
	left join projects p2
	on p1.id = p2.forked_from
	where p1.forked_from is null) as u
group by u.project_id, u.actor) as x
on p.id = x.project_id
where p.forked_from is null;

/* Metric 1: community composition: for each project -> project members, internal collaborators, external collaborators (separated in those ones that got at least a pull request accepted and those one that did not) and external members (all the rest of developers that do not fall in the previous categories) */
drop table if exists op1;
create table op1 (
					project_id int(11),
					project_name varchar(255),
					owner_and_pms numeric(10,6),
					collaborators numeric(10,6),
					external_win_contributors numeric(10,6),
					external_fail_contributors numeric(10,6),
					external_users numeric(10,6),
					number_of_owner_and_pms numeric(11),
					number_of_collaborators numeric(11),
					number_of_external_win_contributors numeric(11),
					number_of_external_fail_contributors numeric(11),
					number_of_external_users numeric(11)
				);
insert into op1 (
					project_id,
					project_name,
					owner_and_pms,
					collaborators,
					external_win_contributors,
					external_fail_contributors,
					external_users,
					number_of_owner_and_pms,
					number_of_collaborators,
					number_of_external_win_contributors,
					number_of_external_fail_contributors,
					number_of_external_users
				)
select 
		p.id as project_id,
		p.name as project_name,
		ifnull(((x.pm_and_owner * 100)/x.all_users),0) as project_members,
		ifnull(((x.internal_collaborators * 100)/x.all_users),0) as internal_collaborators,
		ifnull(((x.external_win_collaborators * 100)/x.all_users),0) as external_win_collaborators,
		ifnull(((x.external_fail_collaborators * 100)/x.all_users),0) as external_fail_collaborators,
		ifnull((((x.all_users - ( x.pm_and_owner + x.internal_collaborators + x.external_win_collaborators + x.external_fail_collaborators)) * 100)/x.all_users),0) as external_users,
		ifnull(pm_and_owner,0) as number_of_project_members,
		ifnull(internal_collaborators,0) as number_of_collaborators,
		ifnull(external_win_collaborators,0) as number_of_win_external_collaborators,
		ifnull(external_fail_collaborators,0) as number_of_fail_external_collaborators,
		ifnull(all_users,0) as number_of_users
from
	projects p
left join
		(select z.project_id,
				ifnull(z.pm_and_owner, 0) as pm_and_owner,
				ifnull(e.internal_collaborators,0) as internal_collaborators,
				ifnull(r.external_win_collaborators,0) as external_win_collaborators,
				ifnull(l.external_fail_collaborators,0) as external_fail_collaborators,
				ifnull(t.all_users,0) as all_users
		from
			(select i.project_id, ifnull(count(distinct i.user_id), 0) as all_users
				from
					(select a.project_id, a.user_id
					from first_user_activity_per_project a
					union
					select a.project_id, a.user_id
					from pm_and_contributors_per_project a
					union
					select a.project_id, a.user_id
					from external_contributors_per_project a
					union
					select a.project_id, a.user_id
					from external_failed_contributors_per_project a) as i
				group by i.project_id) as t
			left join
			(select a.project_id, ifnull(count(distinct a.user_id), 0) as pm_and_owner
			from pm_and_contributors_per_project a
			where a.type_user in ('project_owner', 'project_member')
			group by a.project_id) as z
			on t.project_id = z.project_id
			left join
			(select a.project_id, ifnull(count(distinct a.user_id), 0) as internal_collaborators
			from pm_and_contributors_per_project a
			where a.type_user = 'internal_contributor'
			group by a.project_id) as e
			on t.project_id = e.project_id
			left join
			(select a.project_id, ifnull(count(distinct a.user_id), 0) as external_win_collaborators
			from external_contributors_per_project a
			group by a.project_id) as r
			on t.project_id = r.project_id
			left join
			(select a.project_id, ifnull(count(distinct a.user_id), 0) as external_fail_collaborators
			from external_failed_contributors_per_project a
			group by a.project_id) as l
			on t.project_id = l.project_id) as x
on p.id = x.project_id
where p.forked_from is null;

/* Metric 2: for each project -> external pull requests accepted, total of pull request accepted, average time per project to get a pull request accepted from an external collaborator */
drop table if exists op2;
create table op2 (
					project_id int(11),
					project_name varchar(255),
					external_pull_requests_accepted int(11),
					total_pull_requests_accepted int(11),
					avg_days_to_evaluate_external_contributions numeric(10,6)
				);
insert into op2 (
					project_id,
					project_name,
					external_pull_requests_accepted,
					total_pull_requests_accepted,
					avg_days_to_evaluate_external_contributions
				)
select p.id as project_id, p.name as project_name, ifnull(h.external_pull_requests_accepted,0), ifnull(k.total_pull_requests_accepted,0), ifnull(x.avg_days_to_evaluate_external_contributions,0)
from
	projects p
left join
	(select t.*, avg(timestampdiff(HOUR, t.issue_creation, t.issue_action_time)/24) as avg_days_to_evaluate_external_contributions
		from pr_commit_issue_info_per_project t
		where ((t.merged = 1 and t.pull_request_action is null and t.issue_action is null)
					or
					(t.pull_request_action = 'closed' and t.issue_action is null)
					or
					(t.pull_request_action is null and t.issue_action = 'closed' )
					or
				    (t.pull_request_action = 'closed' and t.issue_action = 'closed'))  
																							and t.pull_requester in (
																														select distinct user_id
																														from external_contributors_per_project
																														where project_id = t.project_id
																														union
																														select distinct user_id
																														from external_failed_contributors_per_project
																														where project_id = t.project_id
																													)
	group by t.project_id
	) as x
on p.id = x.project_id
left join
	(select t.project_id, count(distinct t.pull_request_id) as external_pull_requests_accepted
	from pr_commit_issue_info_per_project t
	where t.merged = 1 and t.pull_requester in (select distinct user_id from external_contributors_per_project where project_id = t.project_id)
	group by t.project_id) as h
on p.id = h.project_id
left join
	(select t.project_id, count(distinct t.pull_request_id) as total_pull_requests_accepted
	from pr_commit_issue_info_per_project t
	where t.merged = 1
	group by t.project_id) as k
on p.id = k.project_id
where p.forked_from is null;

/* Metric 2-pl (focused on external pull requests): for each project -> number of external pull requests accepted, number of external pull request submitted */
drop table if exists op2_pr;
create table op2_pr (
					project_id int(11),
					project_name varchar(255),
					external_pull_requests_accepted int(11),
					total_external_pull_requests int(11)
				);
insert into op2_pr (
					project_id,
					project_name,
					external_pull_requests_accepted,
					total_external_pull_requests
				)
select p.id as project_id, p.name as project_name, ifnull(h.external_pull_requests_accepted,0), ifnull(k.total_external_pull_requests,0)
from
	projects p
left join
	(select t.project_id, count(distinct t.pull_request_id) as external_pull_requests_accepted
	from pr_commit_issue_info_per_project t
	where t.merged = 1 and t.pull_requester in (select distinct user_id from external_contributors_per_project where project_id = t.project_id)
	group by t.project_id) as h
on p.id = h.project_id
left join
	(select t.project_id, count(distinct t.pull_request_id) as total_external_pull_requests
	from pr_commit_issue_info_per_project t
	where t.pull_requester in (select distinct user_id from external_contributors_per_project where project_id = t.project_id)
	group by t.project_id) as k
on p.id = k.project_id
where p.forked_from is null;

/* Metric 3: average time per project to become collaborator */
drop table if exists op3;
create table op3 (
					project_id int(11),
					project_name varchar(255),
					number_of_collaborators numeric(11),
					number_of_users_turned_collaborators numeric(11),
					avg_days_to_become_collaborator numeric(10,6)
				);
insert into op3 (
					project_id,
					project_name,
					number_of_collaborators,
					number_of_users_turned_collaborators,
					avg_days_to_become_collaborator
				)
select p.id as project_id, p.name as project_name, ifnull(z.number_of_collaborators, 0), ifnull(w.number_of_users_turned_collaborators,0), ifnull(w.avg_days_to_become_collaborator,0)
from
	projects p
left join
(select c.project_id, count(c.user_id) as number_of_collaborators
from pm_and_contributors_per_project c
where c.type_user in ('internal_contributor')
group by c.project_id) as z
on p.id = z.project_id
left join
	(select o.project_id, o.project_name, count(o.user_id) as number_of_users_turned_collaborators, avg(o.days) as avg_days_to_become_collaborator
	from
		(select h.project_id, h.project_name, h.user_id, timestampdiff(DAY, j.first_activity, h.first_activity_as_internal_contributors) as days
		from
			(select p.name as project_name, q.*
			from
				projects p
			join
				/* first activity as internal contributors */
				(
				select p.project_id, p.user_id, p.first_action as first_activity_as_internal_contributors
				from pm_and_contributors_per_project p
				where p.type_user in ('internal_contributor')
				) as q
			on p.id = q.project_id
			where p.forked_from is null
			) as h
		join
			/* first no-collaborator activity in the project */
			(select i.project_id, i.project_name, i.user_id, i.first_activity
			from
				(select u.project_id, u.project_name, u.actor as user_id, min(u.time_event) as first_activity
				from (
					/* the actor of the issue event, where the event is not closed or merged*/
					select p.id as project_id, p.name as project_name, ie.actor_id as actor, ie.created_at as time_event
					from issues i, issue_events ie, projects p
					where i.id = ie.issue_id and i.repo_id = p.id and ie.action not in ('closed', 'merged')
					union
					/* the author of an issue comment */
					select p.id as project_id, p.name as project_name, ic.user_id as actor, ic.created_at as time_event
					from issue_comments ic, issues i, projects p
					where i.id = ic.issue_id and i.repo_id = p.id and ic.user_id
					union
					/* the author of a commit comment */
					select p.id as project_id, p.name as project_name, cc.user_id as actor, cc.created_at as time_event
					from commit_comments cc, commits c, projects p
					where cc.commit_id = c.id and c.project_id = p.id
					union
					/* the author of a commit comment - this is needed because some results are lost otherwise */
					select p.id as project_id, p.name as project_name, cc.user_id as actor, cc.created_at as time_event
					from commit_comments cc, project_commits pc, projects p
					where pc.project_id = p.id and cc.commit_id = pc.commit_id
					union
					/* the author of a pull request comment */
					select p.id as project_id, p.name as project_name, prc.user_id as actor, prc.created_at as time_event
					from pull_requests pr, pull_request_comments prc, projects p
					where pr.id = prc.pull_request_id and pr.base_repo_id = p.id
					union
					/* the author of a commit - in GitHub the author does not need to have write access on the repository */
					select p.id as project_id, p.name as project_name, c.author_id as actor, c.created_at as time_event
					from commits c, projects p
					where c.project_id = p.id and ((p.forked_from is null and c.author_id <> c.committer_id) or (p.forked_from is not null))
					union
					/* the author of a commit - in GitHub the author does not need to have write access on the repository - this is needed because some results are lost otherwise*/
					select p.id as project_id, p.name as project_name, c.author_id as actor, c.created_at as time_event
					from project_commits pc, commits c, projects p
					where pc.project_id = p.id and pc.commit_id = c.id and ((p.forked_from is null and c.author_id <> c.committer_id) or (p.forked_from is not null))
					union
					/* the committer of a commit - he cannot be the committer in a no-forked repo */
					select p.id as project_id, p.name as project_name, c.committer_id as actor, c.created_at as time_event
					from commits c, projects p
					where c.project_id = p.id and p.forked_from is not null
					union
					/* the committer of a commit - he cannot be the committer in a no-forked repo - this is needed because some results are lost otherwise */
					select p.id as project_id, p.name as project_name, c.committer_id as actor, c.created_at as time_event
					from project_commits pc, commits c, projects p
					where pc.project_id = p.id and pc.commit_id = c.id and p.forked_from is not null
					union
					/* a pull requester - note that the pull request cannot be an intra branch one */
					select p.id as project_id, p.name as project_name, pr.user_id as actor, prh.created_at as time_event
					from pull_requests pr, pull_request_history prh, projects p
					where pr.id = prh.pull_request_id and pr.base_repo_id = p.id and pr.intra_branch = 0
					union
					/* the reporter of an issue */
					select p.id as project_id, p.name as project_name, i.reporter_id as actor, i.created_at as time_event
					from issues i, projects p
					where i.repo_id = p.id
					union
					/* the assignee of an issue */
					select p.id as project_id, p.name as project_name, i.assignee_id as actor, i.created_at as time_event
					from issues i, projects p
					where i.repo_id = p.id
					union
					/* the owner of a forked project */
					select p1.id as project_id, p1.name as project_name, p2.owner_id as actor, p2.created_at as time_event
					from projects p1
					left join projects p2
					on p1.id = p2.forked_from
					where p1.forked_from is null
					) as u
		group by u.project_id, u.actor) as i) as j
		on h.project_id = j.project_id and h.user_id = j.user_id
		) as o
	where o.days > 0
	group by o.project_id) as w
on p.id = w.project_id
where p.forked_from is null;
/* finish */