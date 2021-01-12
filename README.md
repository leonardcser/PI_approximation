# Project work in progress... 
> **Current state:** early development, functional

## Contents

- [Features](#features)
- [Todo](#todo)
- [Cells vs Pixels](#cells-vs-pixels)
- [How to use (in current state)](#how-to-use)
- [Demos](#demos)
- [Contribution](#contribution)

<a name="features"></a>
## Features
- Keyboard input
- Sound
- Custom sized window
- Fullscreen
- Terminal state save and restore
- System.err logger
- Optimised System.out.print()
- Custom colors for Cells
- Custom colors for Pixels
- And much more customisation...

<a name="todo"></a>
## Todo
[ ] add java doc  
[ ] simplify use  
...

<a name="cells-vs-pixels"></a>
## Cells vs Pixels
A Cell is the outerclass of a Pixel. It contains a x and y position on the Canvas, a Pixel instance and a priority (use for drawing).  
A Pixel contains a char, a background color and a forground color. Using half block chars, it allow to draw perfectly square pixels on the Canvas.

In order to draw anything, cells or pixel must be requested from Canvas.java

<a name="how-to-use"></a>
## How to use (in current state)
1. Create new package in /application
2. Create new class overriding Application.java
3. Create new "states" package in this new package
4. Create new abstract class in "states" package

#### This is an example of how the file tree would look like:
<pre>
.
└── 📦 src/com/leo/application
    └── 📦 "yourapplication"
        ├── "YourApplication.java" ............ overrides Application.java
        └── 📦 states
            ├── "YourApplicationState.java" ... overrides State.java
            └── "YourState.java" .............. overrides "YourApplicationState.java"
</pre>

5. Add a new instance of the Application in [Loop.java](src/com/leo/application/Loop.java) main method

<a name="demo"></a>
## Demos
Check out [/visualiserapp](src/com/leo/application/visualiserapp) and [/snakegame](src/com/leo/application/snakegame). These are for now only proofs of concept and the code is not perfect neither optimised.

<a name="contribution"></a>
## Contribution
Feel free to contact me: leocser632@gmail.com as well as suggesting ideas or features :)
