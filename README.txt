# CPU_Scheduler2
By Brendan Kiernan and Jonathon Lally
For Operating Systems Fall 2018

To Use first choose an algorithm from the algorithm drop down box.
Then specifiy a number of processes (1-100)
Then choose random or fixed values.
If random, bursts will be randomly chosen 1-10.
If fixed values, a new window will open and you can choose how many processes you want with the - + Buttons
After random or fixed is chosen press the calculate button to perform a simulation.

First Come First Serve schedules processes based on order they arrive.  Our simulation assumes they arrive p0,p1,p2 and so on.
Shortest Job First schedules proccesses such that the shortest is performed before longer ones.
Shortest Remaining schedules with processes arriving at different times.  Our simulation has processes arrive each cycle so that
P0 had arrived intially, P1 arrives after 1 cycle, P2 arries after another cycle and so on.  Shortest Remaining checks each process that
has arrives and selects the one with the least amount of remaining time.
Round Robin scheduling requires a time quantum to be entered upon selection.  This time quantum will determine how much time each process
gets before moving on to the next one.  Each process then gets that amount of time then it moves on to the next one.
Priority Scheduling schedules based on a priority value.  More important priority processes will be selected to run before lesser ones.
Lower priority numbers will run before higher priority in our simulation.  Priority will be chosen randomly between 1-10 if random values selected.

Average wait times and turn around times are listed next to average.
