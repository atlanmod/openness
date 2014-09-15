import pygal  
from pygal.style import NeonStyle                                                   
line_chart = pygal.HorizontalStackedBar(
										#title='Time to become collaborator',
										title_font_size=24, fill=True,
										style=NeonStyle,
										#spacing=500,
										#margin=50,
										x_label_rotation=30,
										truncate_legend=65,
										pretty_print=True,
										legend_at_bottom=True,
										width=1500,
										height=2000,
										value_font_size=26,
										legend_font_size=24,
										label_font_size=20,
										tooltip_font_size=28
										)
#line_chart.title = 'Time to become collaborator'
line_chart.x_labels = ['akka',
'devtools',
'ProjectTemplate',
'stat-cookbook',
'hiphop-php',
'knitr',
'shiny',
'folly',
'mongo',
'doom3.gpl',
'phantomjs',
'TrinityCore',
'MaNGOS',
'bitcoin',
'mosh',
'xbmc',
'http-parser',
'beanstalkd',
'redis',
'ccv',
'memcached',
'openFrameworks',
'libgit2',
'redcarpet',
'libuv',
'SignalR',
'SparkleShare',
'plupload',
'mono',
'Nancy',
'ServiceStack',
'AutoMapper',
'RestSharp',
'ravendb',
'MiniProfiler',
'storm',
'elasticsearch',
'ActionBarSherlock',
'facebook-android-sdk',
'clojure',
'CraftBukkit',
'netty',
'android',
'node',
'jquery',
'html5-boilerplate',
'impress.js',
'd3',
'chosen',
'Font-Awesome',
'three.js',
'foundation',
'symfony',
'CodeIgniter',
'php-sdk',
'zf2',
'cakephp',
'ThinkUp',
'phpunit',
'Slim',
'django',
'tornado',
'httpie',
'flask',
'requests',
'symfony',
'reddit',
'boto',
'django-debug-toolbar',
'Sick-Beard',
'django-cms',
'rails',
'homebrew',
'jekyll',
'gitlabhq',
'diaspora',
'devise',
'blueprint-css',
'octopress',
'vinc.cc',
'paperclip',
'compass',
'finagle',
'kestrel',
'flockdb',
'gizzard',
'sbt',
'scala',
'scalatra',
'zipkin',
'Bukkit',
'Overall statistics']
line_chart.add('# of users turned collaborators',
[
28,
5,
0,
0,
5,
4,
0,
1,
0,
0,
8,
1,
0,
26,
2,
31,
4,
3,
18,
0,
0,
21,
15,
0,
11,
11,
5,
3,
0,
14,
0,
3,
6,
0,
4,
5,
20,
9,
0,
0,
0,
10,
0,
0,
4,
18,
2,
10,
3,
7,
25,
39,
162,
6,
0,
13,
0,
6,
7,
9,
0,
16,
2,
0,
23,
0,
14,
24,
6,
7,
16,
217,
356,
21,
52,
36,
7,
0,
29,
1,
12,
22,
7,
0,
5,
5,
13,
23,
2,
0,
0,
16.48
])
line_chart.add('# of remaining collaborators',
[
13,
0,
0,
0,
0,
1,
0,
0,
1,
0,
4,
3,
0,
10,
1,
17,
2,
1,
3,
0,
0,
3,
7,
0,
2,
12,
0,
3,
0,
4,
0,
0,
4,
1,
3,
3,
2,
3,
0,
0,
0,
7,
4,
0,
3,
7,
3,
10,
11,
5,
14,
28,
61,
9,
0,
7,
0,
2,
4,
5,
0,
7,
0,
2,
20,
0,
1,
11,
1,
3,
13,
44,
122,
7,
35,
9,
7,
0,
20,
5,
1,
15,
4,
2,
8,
14,
2,
2,
2,
5,
0,
7.0879
])
line_chart.render_to_file('m3_chart_numbers.svg')