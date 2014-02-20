Analyzing openness of GitHub projects
===============

Visit the [http://atlanmod.github.io/openness] to see the results.

What is this project about?
---------------------------

Open source software projects evolve thanks to a group of volunteers that help in their development. Thus, the success of these projects depends on their ability to attract (and capture) developers. The openness of a project, that is, the tendency to accept changes from non-project members and to allow them to participate in the decision-making process,
can help to make a project more attractive and therefore to absorb new developers. To explore the openness of a software project, we propose three metrics focused on:

* the study of the developer community of the project (how the community of the project is composed in terms of project members and non-project members?)
* the analysis of accepted external contributions (can non-project members contribute to the project? how successful is their contribution? how long does it take to successfully contribute to the project?)
* the time required to become contributor of the project (how long does it take to be part of the decision-making process of a project?)

These metrics are adapted to GitHub projects, which is arguably the largest and best-known social coding site, containing more than 10 million repositories, and has become one of the main references in open software development.


What can you find in this repository?
-------------------------------------

* metrics/msr-metrics.sql. It contains an SQL script implementing the metrics previously presented. The metrics are applied on a subset of GitHub projects, downloadable [http://ghtorrent.org/msr14.html]
* gh-pages. It contains the web-page of the study, with charts and statistics for each GitHub project analyzed


What is coming next?
--------------------
We would like to refine our metrics in order to identify possible threshold values which may help to precisely classify a project. 

Who is behind this project?
---------------------------
* [Javier Canovas](http://github.com/jlcanovas/ "Javier Canovas")
* [Valerio Cosentino](https://github.com/valeriocos/ "Valerio Cosentino")
* [Jordi Cabot](http://github.com/jcabot/ "Jordi Cabot")

Javier, Valerio and Jordi work in [Atlandmod](http://www.emn.fr/z-info/atlanmod), a research team of Inria.
