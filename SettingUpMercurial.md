## Setup ##
  1. Download [Eclipse IDE for Java Developers](http://www.eclipse.org/downloads/packages/eclipse-ide-java-developers/heliossr2)
  1. Install the Mercurial plugin in Eclipse:
    * Open Eclipse
    * Help > Install New Software
    * Add
      * Name: Mercurial
      * URL: http://cbes.javaforge.com/update
    * Select "MercurialEclipse"
    * Continue until it's installed and restart
  1. Pull code from repository:
    * File... New Project
    * Mercurial > Clone Existing
    * Input the URL found in the "Source" section of this site
    * Input your email, and the password found in the "Source" section of this site (if you want to commit changes)

## Getting Familiar ##
  * You should see two projects in the package explorer pane:
    1. GdxTest: The game, with an application launcher for the desktop
    1. GdxTest-Android: The application launcher for android
  * All the game code is in GdxTest.  The GdxTest-Android is just an Android application that references the game code in GdxTest.
  * **To run the game**:
    1. Right click "GdxTest" in the package explorer pane on the left
    1. Run As > Java Application
    1. If asked, select "GdxTestDesktop - com.gdx.test"
    1. After the first time you've done this, you should be able to quickly run the game with the green play button in the toolbar.

## Committing Changes ##
Everyone who clones the repository has a local copy of the entire history of the project on their computer.  So when you select the command to **commit** your changes, you're only updating your _local_ copy of the repository.  To update the googlecode repository, first **commit** the changes, then select the **push** command.

  1. Right click project > Team
  1. Select commit to commit the changed files to your local repo
  1. Select push to push your local repo to google code

## Misc Notes ##
  * Make sure to use the HTTPS url when pushing updates, or else you get a 403 Forbidden error
  * Make sure to use the special password found in the "Source" section of this site, not your gmail password
  * The long hex number after the project name in the workspace is the SHA-1 hash generated based on the content of the current branch.  The idea is that it will be unique for each step in the project.
  * 