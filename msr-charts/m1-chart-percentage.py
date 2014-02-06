import pygal  
from pygal.style import NeonStyle                                                   
line_chart = pygal.HorizontalStackedBar(
										#title='Community composition',
										title_font_size=24, fill=True,
										style=NeonStyle,
										#spacing=200,
										#margin=50,
										x_label_rotation=20,
										truncate_legend=65,
										pretty_print=True,
										width=2000,
										height=2000,
										value_font_size=26,
										legend_font_size=24,
										label_font_size=20,
										tooltip_font_size=28,
										#y_title='Original GitHub projects',
										#x_title='Percentage of user types'
										)
#line_chart.title = 'Community composition (in %)'
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
line_chart.add('Project members', [1.983471,
1.877934,
11.111111,
2.380952,
0.658328,
0.290698,
9.150327,
2.884615,
3.392706,
0.160514,
0.171086,
2.117647,
0.344828,
0.434513,
0.411523,
3.5913,
1.219512,
0.318471,
0.081967,
0.322581,
0.63593,
1.68244,
6.935123,
0.911162,
1.449275,
0.662651,
0.218818,
0.323102,
7.378855,
0.505902,
0.895522,
0.46875,
1.246106,
1.295337,
1.049869,
0.549451,
0.0407,
0.065274,
3.460452,
1.113173,
0.873908,
1.303993,
2.379286,
0.28615,
0.247054,
0.139362,
0.027293,
0.05015,
1.230884,
0.038373,
0.022707,
0.586332,
0.301659,
0.579268,
2.990518,
0.44428,
1.056958,
0.470588,
0.218818,
0.137552,
0.068752,
0.276091,
0.265252,
0.298656,
0.261233,
1.075269,
0.860215,
0.515464,
0.13089,
0.155039,
0.704722,
0.284266,
0.054611,
0.148192,
0.296406,
0.77243,
0.715801,
0.915751,
0.095939,
0.952381,
3.047542,
0.349825,
1.4,
1.760563,
65.966387,
65.690377,
0.165837,
2.047244,
4.255319,
65.828092,
100,
4.46742478
])
line_chart.add('Collaborators',  [6.77686,
0,
0,
0,
0.131666,
0,
0,
0,
0.084818,
0,
0,
0.313725,
0,
0,
0,
0.050582,
0.304878,
0,
0.040984,
0,
0,
0.525762,
1.789709,
0,
0.161031,
0.722892,
0,
0.161551,
0,
0,
0,
0,
0.155763,
0.259067,
0.262467,
0,
0.569801,
0,
0,
0,
0,
0.407498,
0.139958,
0,
0.133029,
0.046454,
0,
0.351053,
0.522193,
0.095932,
0,
1.091791,
0.538677,
0.457317,
0,
0.740466,
0,
0,
0,
0,
0,
0,
0,
0,
0.783699,
0,
0,
0.257732,
0.39267,
0.775194,
0.493305,
0.2689,
0.006826,
0.296384,
0.203779,
0.237671,
0.079533,
0,
0.319795,
2.857143,
0.081268,
0.89955,
1.4,
0.704225,
2.731092,
3.974895,
0.829187,
0.314961,
0,
0.838574,
0,
0.391014363
])
line_chart.add('External contributors with at least one contribution accepted',[5.785124,
5.633803,
8.333333,
2.380952,
0.592495,
8.430233,
2.614379,
0,
2.629347,
0.481541,
1.839179,
10.823529,
0.804598,
5.152079,
1.508916,
9.205867,
2.743902,
3.821656,
2.5,
0.645161,
0.158983,
7.045216,
24.832215,
10.250569,
0,
2.228916,
4.376368,
2.504039,
7.819383,
20.74199,
26.41791,
0.9375,
11.370717,
5.181347,
19.685039,
3.228022,
0.569801,
1.305483,
0.070621,
0,
0.499376,
2.03749,
0.139958,
0.362457,
0.760167,
1.703314,
0.709607,
0.902708,
1.902275,
0.287797,
3.315168,
5.29721,
10.148675,
6.95122,
0.510576,
8.922621,
8.338227,
4,
5.032823,
4.539202,
7.390856,
5.245721,
5.039788,
5.873569,
13.793103,
0,
0.286738,
18.363402,
6.675393,
5.271318,
8.45666,
7.075907,
0.061438,
4.119739,
6.33568,
5.941771,
4.798515,
0.18315,
2.622322,
60,
3.372613,
3.348326,
2.2,
3.169014,
0.420168,
0.627615,
4.975124,
14.330709,
16.170213,
0.628931,
0,
5.58009744
])
line_chart.add('External contributors with no contribution accepted',[3.305785,
4.225352,
2.777778,
0,
0.724161,
0,
0,
0.480769,
3.986429,
0.642055,
1.112062,
7.215686,
0.45977,
2.66915,
0.823045,
2.377339,
3.353659,
1.592357,
1.844262,
0.645161,
0.63593,
1.26183,
3.803132,
0.683371,
8.856683,
1.26506,
0.218818,
0.969305,
2.53304,
3.204047,
0.447761,
2.65625,
2.024922,
7.512953,
1.049869,
1.236264,
1.058201,
1.370757,
0.564972,
2.040816,
13.483146,
3.015485,
1.119664,
5.074399,
3.534778,
1.610406,
0.682314,
0.802407,
2.163372,
0.422103,
1.702997,
0.545896,
6.313295,
4.481707,
1.021152,
9.070715,
6.34175,
2.117647,
2.552881,
6.327373,
7.047095,
3.257869,
2.387268,
1.642608,
1.933124,
1.075269,
4.37276,
2.963918,
1.832461,
6.821705,
2.396054,
2.61217,
12.314834,
1.719028,
2.463876,
2.020202,
1.802757,
0.549451,
2.878158,
5.714286,
2.519301,
1.89905,
2.4,
0.704225,
0.420168,
0,
1.824212,
2.047244,
0.425532,
0.419287,
0,
2.576661538
])
line_chart.add('External members',  [82.14876,
88.262911,
77.777778,
95.238095,
97.893351,
91.27907,
88.235294,
96.634615,
89.906701,
98.715891,
96.877673,
79.529412,
98.390805,
91.744258,
97.256516,
84.774911,
92.378049,
94.267516,
95.532787,
98.387097,
98.569157,
89.484753,
62.639821,
88.154897,
89.533011,
95.120482,
95.185996,
96.042003,
82.268722,
75.548061,
72.238806,
95.9375,
85.202492,
85.751295,
77.952756,
94.986264,
97.761498,
97.258486,
95.903955,
96.846011,
85.143571,
93.235534,
96.221134,
94.276994,
95.324971,
96.500465,
98.580786,
97.893681,
94.181276,
99.155794,
94.959128,
92.478771,
82.697694,
87.530488,
95.477753,
80.821918,
84.263065,
93.411765,
92.195478,
88.995873,
85.493297,
91.22032,
92.307692,
92.185167,
83.22884,
97.849462,
94.480287,
77.899485,
90.968586,
86.976744,
87.94926,
89.758758,
87.562291,
93.716657,
90.700259,
91.027926,
92.603393,
98.351648,
94.083786,
30.47619,
90.979277,
93.503248,
92.6,
93.661972,
30.462185,
29.707113,
92.205638,
81.259843,
79.148936,
32.285115,
0,
86.98480187
])
line_chart.render_to_file('m1_chart_percentage.svg')