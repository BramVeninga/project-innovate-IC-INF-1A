from machine import Pin
import time

# LED is verbonden met pin GP0 (GPIO0)
led = Pin('LED', Pin.OUT)

while True:
    led.toggle() # LED aan/uit zetten
    time.sleep(0.5) # pauze van 0,5 seconden
