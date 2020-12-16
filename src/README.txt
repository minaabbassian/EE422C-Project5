/*
 * CRITTERS GUI 
 * EE422C Project 5 submission by
 * Replace <...> with your actual data.
 * Mina Abbassian
 * mea2947
 * 16170
 * Abdullah Haris
 * ah52897
 * 16185
 * Slip days used: <0>
 * Git URL: https://github.com/EE422C/fall-2020-pr5-fa20-pr5-pair-80.git
 * Fall 2020
 */

Our window consists mainly of a tabpane with 3 tabs: World Animation, Statistics, and Parameters.

The World Animation tab consists of everything we could think of that could be useful for the user 
while viewing the world. We have our world embedded in a scrollable pane so that the user can create 
a world size larger than the size of their screen, and have a good experience using it.

Next to the scrollable world, we have options to set the speed of the animation and the ability to 
start and stop it. Underneath the scrollable world, we have a dropdown menu for selecting a critter 
and a text box to create any reasonable number of that critter. We also have a text field for 
advancing the world an arbitrary number of steps. We have a button to clear the world and set it to 
a random color, and we have background music on launch as well. When Critters are created manually, 
a sound effect is played.

The Statistics tab contains live statistics about every critter. By default, all the statistics 
are invisible, so that the user isn’t overwhelmed with information. Checking individual critters 
will reveal it’s statistics.

The Parameters tab is used to change world parameters during runtime. This feature came really 
handy when debugging. All the parameters can be changed even when the animation is in progress.
