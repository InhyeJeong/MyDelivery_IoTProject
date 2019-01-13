# -*- coding: utf-8 -*- 
from __future__ import print_function
import pyzbar.pyzbar as pyzbar
import numpy as np
import cv2
import requests
import time
import serial
import threading


serial_small = serial.Serial(port='/COM5', baudrate=9600)
#serial_large = serial.Serial(port='/COM12', baudrate=9600)
#serial = [serial_small, serial_large]
serial = [serial_small, serial_small]
server_ip = 'http://192.168.0.5:3000/'
lockers = ['empty', 'empty']
def sender_open_locker(senderQR, lockerNumber):
  # 서버에 post 요청
  URL = server_ip + 'senderOpen' # senderOpen senderClose receiverOpen receiverClose
  data = {'locationCode': '1', 'senderQR':senderQR, 'lockerNumber':lockerNumber}
  res = requests.put(URL, data= data)
  data = res.json()
  print(data)
  if data[0] == 1: # 일치하는 항목이 있는 경우 -> 아두이노에 문을 열라는 명령을 내린다.
    print('open door')
    lockers[lockerNumber] = 'full'
    serial[lockerNumber].write(str.encode('o')) #open 명령
    ch = serial[lockerNumber].readline().decode("utf-8")[0] # 아두이노로부터 문이 닫혔다는 신호를 기다림
    #print (ch)
    if(ch == 'c'):
      print('close door')
      URL = server_ip + 'senderClose' # senderOpen senderClose receiverOpen receiverClose
      data = {'locationCode': '1', 'senderQR':senderQR}
      requests.put(URL, data= data)      


    
  
def decode(im) : 
  # Find barcodes and QR codes
  decodedObjects = pyzbar.decode(im)
 
  # Print results
  #for obj in decodedObjects:
    #print('Type : ', obj.type)
    #print('Data : ', obj.data,'\n')
     
  return decodedObjects
 
 
# Display barcode and QR code location  
def display(im, decodedObjects):
 
  # Loop over all decoded objects
  for decodedObject in decodedObjects: 
    points = decodedObject.polygon
 
    # If the points do not form a quad, find convex hull
    if len(points) > 4 : 
      hull = cv2.convexHull(np.array([point for point in points], dtype=np.float32))
      hull = list(map(tuple, np.squeeze(hull)))
    else : 
      hull = points
     
    # Number of points in the convex hull
    n = len(hull)
 
    # Draw the convext hull
    for j in range(0,n):
      cv2.line(im, hull[j], hull[ (j+1) % n], (255,0,0), 3)
 
  # Display results 
  # cv2.imshow("Results", im)
  # cv2.waitKey(0)
 
 
   
# Main 
if __name__ == '__main__':
  
  cap = cv2.VideoCapture(0)
  detected = 0
  while(True):
    # 카메라에서 이미지 읽어오기
    ret, frame = cap.read()
  
    decodedObjects = decode(frame)
    display(frame, decodedObjects)

    # 이미지 좌우 반전
    frame = cv2.flip(frame,1)

    if(detected > 0):
      # 화면 우측을 파란색으로
      for i in range (480):
        for j in range (150):
          frame[i][j][0] = 255

          
      # 화면 좌측을 초록색으로
      for i in range (480):
        for j in range (150):
          frame[i][639-j][1] = 255
          
      # 화면 왼쪽에 글자 쓰기 (small)
      cv2.putText(frame, "small", (0, 240), cv2.FONT_HERSHEY_COMPLEX, 1, (0, 0, 0))
      cv2.putText(frame, lockers[0], (0, 300), cv2.FONT_HERSHEY_COMPLEX, 1, (0, 0, 0))

      # 화면 오른쪽에 글자 쓰기 (large)
      cv2.putText(frame, "large", (500, 240), cv2.FONT_HERSHEY_COMPLEX, 1, (0, 0, 0))
      cv2.putText(frame, lockers[1], (500, 300), cv2.FONT_HERSHEY_COMPLEX, 1, (0, 0, 0))

    if(len(decodedObjects) == 1): ## QR코드가 1개 인식됨
      detected = 30
      QRcode = decodedObjects[0].data.decode("utf-8")
      
      ## QRcode가 0으로 시작하면 -> 발신인
      if(QRcode[0] == '0'):
          
        if(len(decodedObjects) == 1):
          if(decodedObjects[0].polygon[0][0] > 500 and lockers[0] == 'empty'):
            #print ("small")
            t = threading.Thread(target=sender_open_locker, args=(QRcode, 0))
            t.start()
              
            
          elif(decodedObjects[0].polygon[0][0] < 140 and lockers[1] == 'empty'):
            #print ("large")
            t = threading.Thread(target=sender_open_locker, args=(QRcode, 1))
            t.start()
            

      ## QRcode가 1로 시작하면 -> 수신인
      elif(QRcode[0] == '1'):
        a = 1
        ## 서버에 QR, 지역코드를 전송해서 결과값 받아옴
        ## 결과값이 1
          ## 결과값에서 보관함번호를 알아내어 해당 보관함 열어줌
        ## 결과값이 0
          ## 해당 QRcode와 일치하는 항목이 없음을 출력 후 pass

      ## 아두이노로부터 

    else:
      detected -= 1

    

    
        
    # 화면 출력
    cv2.imshow("frame", frame), cv2.waitKey(1)
  cv2.destroyAllWindows()
  cap.release()

