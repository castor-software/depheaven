package se.kth.castor;

import java.io.File;
import java.io.FileReader;

import org.apache.maven.execution.MavenSession;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.ProjectBuilder;
import org.apache.maven.repository.RepositorySystem;
import org.apache.maven.shared.dependency.graph.DependencyGraphBuilder;

/**
 * This Maven mojo fixes the build failures due to dependency conflicts in a Maven project.
 */
@Mojo(name = "depheaven", defaultPhase = LifecyclePhase.PACKAGE,
   requiresDependencyCollection = ResolutionScope.TEST,
   requiresDependencyResolution = ResolutionScope.TEST, threadSafe = true)
public class DepHeavenMojo extends AbstractMojo {

   /**
    * The Maven project to analyze.
    */
   @Parameter(defaultValue = "${project}", readonly = true)
   private MavenProject project;

   /**
    * The Maven session to analyze.
    */
   @Parameter(defaultValue = "${session}", readonly = true)
   private MavenSession session;

   /**
    * Skip the plugin execution completely.
    */
   @Parameter(defaultValue = "false")
   private boolean skipDepHeaven;

   @Component
   private ProjectBuilder mavenProjectBuilder;

   @Component
   private RepositorySystem repositorySystem;

   @Component(hint = "default")
   private DependencyGraphBuilder dependencyGraphBuilder;

   @Override
   public void execute() {

      if (skipDepHeaven) {
         getLog().info("Skipping DepHeaven plugin execution");
         return;
      }

      this.getLog().info("------------------------------------------------------------------------");
      this.getLog().info("S T A R T I N G    D E P H E A V E N    P L U G I N ");
      this.getLog().info("------------------------------------------------------------------------");

      File pomFile = new File(project.getBasedir().getAbsolutePath() + File.separator + "pom.xml");

      String packaging = project.getPackaging();
      if (packaging.equals("pom")) {
         getLog().info("Skipping because packaging type is " + packaging + ".");
         return;
      }

      /* Build maven model to manipulate the pom */
      Model model;
      FileReader reader;
      MavenXpp3Reader mavenReader = new MavenXpp3Reader();
      try {
         reader = new FileReader(pomFile);
         model = mavenReader.read(reader);
         model.setPomFile(pomFile);
      } catch (Exception ex) {
         getLog().error("Unable to build the maven project.");
         return;
      }

      /* Printing the results to the console */
      this.getLog().info("------------------------------------------------------------------------");
      this.getLog().info("D E P H E A V E N    P L U G I N    F I N I S H E D");
      this.getLog().info("------------------------------------------------------------------------");

   }
}
