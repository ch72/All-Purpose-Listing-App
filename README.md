# The All-Purpose-Listing-Application

**Functions and Features**

This is a modular listing application that makes it extremely easy 
to list out a series of items whether its a shopping list or a list of 
brainstormed ideas. 

The app has two informational columns alongside the main item in the date and
score. The date is useful for longer term lists, like for logging significant 
events or making small journal entries while the score is useful in comparing 
items in the list, whether its ranking video games or sports players.

The top bar contains extra features: \
File -> Save: App autosaves entries, but scores will only be saved using Save.

Edit -> Change Color: Allows background color of list entries to be changed \
Edit -> Hide Score: Hides the entire score column

Note: I wanted to make the list app directory easy to find, so it will always
      appear in the same location as the application. This means that if the
      app is moved, it may be disconnected from its files, so make sure list 
      app directory is moved to that same location as well.

**How To Use**

Mac: Execute GUIProject.jar

Windows: Open up command prompt, change to proper directory, and input:

      javac Driver.java
      java Driver
      
If javac does not work, download a JDK and set up environment variables
(System -> Advanced System Settings -> Environment Variables, then edit
Path variable and add JRE and JDK)

Note: Executing on Windows can be a little messy, so if possible, try to
      convert GUIProject.jar into an executable to allow for cleaner
      execution (I'm working on a fix for this)
