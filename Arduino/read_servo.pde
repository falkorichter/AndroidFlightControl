// Reading servos with interrupts  
 // For an Arduino Mega  
 // Scott Harris January 2010  
 //  
 // This work is licensed under the Creative Commons Attribution-Share Alike 3.0 United States License.   
 // To view a copy of this license, visit http://creativecommons.org/licenses/by-sa/3.0/us/   

 #include <Servo.h>  

 volatile long servo1; // servo value  
 volatile long count1; // temporary variable for servo1  

 #define int0 (PINE & 0b00010000) // Faster than digitalRead  
 //#define int0 (PIND & 0b00000010) //For Duemilenove. Untested! 
 
 Servo myServo;  

 void handleInterrupt()  
 {  
  if(int0)  
    count1=micros(); // we got a positive edge  
  else  
   servo1=micros()-count1; // Negative edge: get pulsewidth  
 }  

 void setup()  
 {  
  Serial.begin(9600);  
  pinMode(2,INPUT);  
  attachInterrupt(0,handleInterrupt,CHANGE); // Catch up and down  
  myServo.attach(9);  
 }  

 void loop()  
 {  
  delay(10);  
  Serial.println(servo1,DEC); // Pulsewidth in microseconds  
  myServo.writeMicroseconds(servo1); // Mirror servo on another pin  
 }  