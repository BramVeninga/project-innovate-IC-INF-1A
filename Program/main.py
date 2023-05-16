from Backpack import *
from Sensor import *
from Compartment import *
from time import sleep
from machine import Pin, UART

sensorsComp0 = (Sensor(0), )
sensorsComp1 = (Sensor(1), Sensor(2))
sensorsComp2 = (Sensor(3), )
sensorsComp3 = (Sensor(4), )

sensors = (sensorsComp0, sensorsComp1, sensorsComp2, sensorsComp3)

backpack = Backpack()
backpack.addMultipleCompartments(sensors)


while 1:  
    backpack.test()
    sleep(1)
    
    
