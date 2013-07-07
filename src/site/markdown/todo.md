Todo
====

### Sites

* Check if artifact versions are up to date in listing pages:
    * http://maven.apache.org/plugins/
    * http://maven.apache.org/shared/
    * http://maven.apache.org/poms/
    * http://maven.apache.org/skins/


# Done

* take screenshot of site. ( os specific )

* See where to publish the result on Maven site
    * jenkins site URL https://builds.apache.org/view/M-R/view/Maven/job/dist-tool-plugin/site

### Source Release
* add date of release (from artifact)
  * date from metadata is ok ?

* Check older artifact in dist (only latest should be present, previous version removed)

* Shell command for cleaning older artifacts

### Sites

* add date of site
    * [sky] date from comment part in skin (no always ok :( )
* remove "check if version present", since our inheritance ensures we don't have problems
    * [sky] kept only for precise layout check
* replace Skins columns with one unique column "Skin used", showing which skin is used (with version)
    * [sky] no garantee some site like http://maven.apache.org/plugins/maven-one-plugin/ have no style header 
* add date of release (from artifact)
    * [sky] date from metadata is ok ?
