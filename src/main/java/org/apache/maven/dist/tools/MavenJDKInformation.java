package org.apache.maven.dist.tools;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

public class MavenJDKInformation
{
    private String pluginName;

    private ArtifactVersion mavenVersion;

    private String jdkVersion;

    public MavenJDKInformation( String pluginName, String mavenVersion, String jdkVersion )
    {
        this.pluginName = pluginName;
        this.mavenVersion = new DefaultArtifactVersion( mavenVersion );
        this.jdkVersion = jdkVersion;
    }

    public ArtifactVersion getMavenVersion()
    {
        return mavenVersion;
    }

    public void setMavenVersion( String mavenVersion )
    {
        this.mavenVersion = new DefaultArtifactVersion( mavenVersion );
    }

    public String getJdkVersion()
    {
        return jdkVersion;
    }

    public void setJdkVersion( String jdkVersion )
    {
        this.jdkVersion = jdkVersion;
    }

    public String getPluginName()
    {
        return pluginName;
    }

    public void setPluginName( String pluginName )
    {
        this.pluginName = pluginName;
    }

}
