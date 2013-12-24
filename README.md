Team18
======

SimCity201 Project Repository - Omar Khulusi, Eric Gauderman, Yixin Cai, Ryan Hsu, and Tanner Zigrang

###Run Instructions

1. Download the project to the computer using
"git clone https://github.com/okhulusi/CSCI201_Final-Project_SimCity.git" or by
manually downloading the zip file from GitHub

2. Open eclipse and create a new java project.
Remember to include JRE System Library (JavaSE-1.7), and JUnit 4 Library

3. Import "source" folder into the project. Right click the "source" folder.
In "Bulid Path", choose "Use as a Source Folder"

4. Choose MainGui.java under source/gui. Click "Run as..." and choose 
"Java Application" to run the program.

5. To run scenarios, choose the Configuration tab at the top right corner, and select a configuration. Descriptions of each scenario will be displayed.  Make sure to click the start button once you have selected a scenario.  To run a second scenario, we recommend you quit the program and restart so as to assure they work correctly.

###V2 Deliverable Information

####Work Done by Each Person

- Omar Khulusi (Team Leader)
  - Bank Scenarios (+ Robber/GuardDog for v2)
  - Console Log integration
  - Configuration Panel v2 upgrades (Upgraded configuration panels to run scenarios)
  - Gui Panel Upgrades (took over for Tanner)
  - Animation Panels (Building interior animation panels and worldview specifically)
  - Integration, major contributor (with Eric and Yixin)
  - Aided Eric with integration of Person Agent and Roles
  - Aided with Person Agent scheduler (worked closely Eric and Yixin)
  - Design of program's architecture (i.e. packages and class hierarchy)
  - Integrated his Restaurant (including revolving stand, market interaction, bank interaction)
  - Time class
  - Discussing major decisions (particularly with Eric and Yixin)
  - Bug fixing
- Eric Gauderman (Integration Lead/Git Lead)
  - Person Agent (sophisticated upgrade in v2)
  - Home Scenarios (most importantly, HomeOccupantRole)
  - Integration, major contributor (with Omar and Yixin)
  - Instantiation of Person Agent with the correct job and house; use of factory methods (with Yixin)
  - Design of program's architecture (i.e. packages and class hierarchy) 
  - Designed Directory
  - Integrated his Restaurant (including revolving stand, market interaction, bank interaction)
  - Implemented hacks in PersonAgent for running specific scenarios
  - General helping with questions & issues
  - Fixed naming conventions for v2
  - Bug fixing
- Yixin Cai (Restaurant Integration Lead, Largest Code Contributor)
  - First to fully integrate his Restaurant, and assisted Omar/Ryan/Eric in integrating restaurants
  - Finalized worldview (added lanes/semaphores/navigation system)
  - Added most non-normative scenarios
  - Workplace and person integration
  - Market design and scenarios (including interactions with all the restaurants)
  - Extensive Testing of city scenarios
  - Instantiation of Person Agent with the correct job and house; use of factory methods (with Eric)
  - Integration, major contributor (with Eric and Omar)
  - Bug fixing
- Ryan Hsu (Art Lead)
  - Transportation (TruckAgent, BusAgent, CommuterRole)
  - Integrating Truck and Market/Truck and Restaurant scenarios
  - Truck Gui
  - Integrated his Restaurant (integrated with revolving stand/market interaction/bank interaction)
  - Added all images and made them look spectacular
  - Worked with each individual restaurant panel to standardize them to look the same
- Tanner Zigrang -- Going to be assessed individually (Talked to Professor Wilcynski regarding this)
  - Gui Panel Setup (more specifically, creation/configuration buttons/forms, organization of frames and panels)
  - Bank unit testing
  - Did not integrate restaurant into team's final deliverable
  - Worked on his restaurant on his own branch

####Known Issues
  - No collision detection in restaurants
