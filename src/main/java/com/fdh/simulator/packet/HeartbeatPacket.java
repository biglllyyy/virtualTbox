package com.fdh.simulator.packet;


public class HeartbeatPacket
{
  private static final byte[] heartbeatPacket = new byte[16];

  static
  {
    heartbeatPacket[0] = 0x14;
    heartbeatPacket[1] = 0x17;
    heartbeatPacket[2] = 0;
    heartbeatPacket[3] = 0x10;

    heartbeatPacket[4] = 0;
    heartbeatPacket[5] = 0;
    heartbeatPacket[6] = 0;
    heartbeatPacket[7] = 0x08;

    heartbeatPacket[8] = 0x21;
    heartbeatPacket[9] = 0x22;
    heartbeatPacket[10] = 0x23;
    heartbeatPacket[11] = 0x24;

    heartbeatPacket[12] = 0x01;
    heartbeatPacket[13] = 0;
    heartbeatPacket[14] = 0x10;
    heartbeatPacket[15] = 0x19;
  }
}