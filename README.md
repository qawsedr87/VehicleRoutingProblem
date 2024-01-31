# Vehicle Routing Problem

## Introduction 
For this Vorto Algorithmic Challenge, you will submit a program that solves a version of the Vehicle Routing Problem (VRP).

In the real repo, I should ignore compiled files, but for testing purpose, I left the files and test cases on this repo. 

## How to compile 
```shell
javac VRPSolver.java

jar cfe VRPSolver.jar VRPSolver VRPSolver.class Load.class Driver.class

# for single file testing
java -jar VRPSolver.jar {file_path}

# for the folder files testing
python3 evaluateShared.py --cmd "java -jar VRPSolver.jar" --problemDir ./trainingProblems
```