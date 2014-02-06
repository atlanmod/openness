import pygal  
from pygal.style import NeonStyle                                                   
line_chart = pygal.HorizontalBar(
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
line_chart.add('Average number of days to become collaborator',
[
205.36,
None,
None,
None,
415.8889,
None,
None,
None,
927.5,
None,
None,
881.5833,
None,
None,
None,
148.051,
161.4444,
None,
228.8696,
None,
None,
341.1622,
157.2222,
None,
149.8889,
100.8,
None,
54.2857,
None,
None,
None,
None,
143.1875,
6,
64,
None,
372.9091,
None,
None,
None,
None,
211.8519,
73.1667,
None,
1057.6667,
181.9697,
None,
107,
549.9048,
97.3846,
None,
274.2297,
165.6538,
778.2381,
None,
504.1667,
None,
None,
None,
None,
None,
None,
None,
None,
73.3191,
None,
None,
91.9268,
418.4286,
601.25,
317.8421,
344.2787,
145.6639,
301.4545,
74.0891,
164.3279,
138.2857,
None,
72.8039,
211.3333,
298.875,
244.2381,
317.2667,
492.2,
188.8947,
386,
257.2667,
467.6471,
None,
2.5556,
None,
64.01126202,
285.0884286
])
line_chart.render_to_file('m3_chart_average.svg')