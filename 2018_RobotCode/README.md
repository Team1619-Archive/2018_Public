# 2018_RobotCode
Main repository for 2018 season

Master contains file common to all projects (branches). 
- Thees common files are often referred to as the framework.
- The current projects (branches) are Framework, ProtoBot, Upstream, PowerUp, PowerUp_Dev, and PowerUp_CompetitionX

Each branch is a compilable/deployable project that contains the common files from Master plus project specific implementation files.
- The implementation files include but are not limited to (.project, XXX_Notes.txt, Constants.java, IO.java, Robot.java, Subsystems.java, and files in the states folder)

Framework is a branch used to develop and test the common files stored in Master.
- Framework is branched from the Master branch.
- Changes to the common files are merged into Master to be used by the other projects (branches).
- Changes to the project specific implementation files are not merged into Master.

ProtoBot is a branch used to develop and test code deployed to the ProtoBot robot.
- ProtoBot is branched from the Master branch.
- Changes in this branch should never be merged into Master.
- When a bug is fixed in the common files, that fix should be merged into Framework for testing.

Upstream is a branch used to re-develop code deployed to the 2017 Upstream robot as a way to test the new framework.
- Upstream is branched from the Master branch.
- Changes in this branch should never be merged into Master.
- When a bug is fixed in the common files, that fix should be merged into Framework for testing.

PowerUp is a branch used to store known good versions (releases) of code deployed on the 2018 robot.
- PowerUp is branched from the Master branch.
- Code from this branch will be used at competitions.
- This branch should only receive occasional updates of fully tested code ready to be deployed on the 2018 robot.
- All development work should be done in the PowerUp_Dev branch.
- Changes to the common files should come from PowerUp_Dev via Master.
- Changes to the implementation files should come from PowerUp_Dev.
- Changes in the PowerUp_Dev branch should be merged into the PowerUp branch at tested milestones.
- Changes in PowerUp should never be merged into Master.
- Bugs found in the common files should be fixed in the Framework or PowerUp_Dev branch and then merged into Master.

PowerUp_Dev is a branch used to develop and test code for the 2018 robot. 
- PowerUp_Dev is branched from the PowerUp branch.
- Changes to this branch should be merged into the PowerUp branch only at known, tested milestones (releases)

PowerUp_CompetitionX is a branch created for use at a specific competition to track changes at that competition.
- PowerUp_CompetitionX is branched from PowerUp.
- This branch should be used for changes made at the competition with frequent commits.
- The goal is to track every change from a specific competition so they can be reviewed after the competition is over.
- The many changes to this branch from a specific competition should be merged into PowerUp_Dev after the competition and tested. 
- Once tested, changes to the common files should be merged from PowerUp_Dev to Master as a single commit labeled for that competition.
- Once tested, changes to the implementation files should be merged along with those from Master to PowerUp as a single commit labeled for that competition.

A developer can easily create new branches from any of the other branches for specific purposes.
- This can be done when one wants to explore an idea knowing the code will probably be thrown away.
- This can be done to work on a specific feature.
- This can be done to create variation such as multiple versions for different tests on the ProtoBot.
- This can be done to give a new programmer code to learn on knowing their changes will be thrown away.
- A new programmer can also fork the entire project into their own GitHub account to play with.
