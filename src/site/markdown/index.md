About dist-tool-plugin
=====

The dist-tool-plugin checks that Maven release rules are well applied across every artifact.

Results are displayed in 3 reports:

* [Dist Tool> Sites][1] report, for checks about sites associated to artifacts,

* [Dist Tool> Source Release][2] report, for checks about artifacts source release publication.

* [Dist Tool> Index page][3] report, for checks about index pages.

This is [Work In Progress][4]...

Notice that this plugin is actually intended for Maven itself only: if interest is expressed to use it
in other context, it would require more configurations.

[1]: ./dist-tool-check-site.html
[2]: ./dist-tool-check-source-release.html
[3]: ./dist-tool-check-index-page.html
[4]: ./todo.html

http://maven.apache.org/developers/release/maven-project-release-procedure.html#Copy_the_source_release_to_the_Apache_Distribution_Area
        