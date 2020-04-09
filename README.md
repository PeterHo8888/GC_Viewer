# GC_Controller

The purpose of this is to act similar to NintendoSpy in that it shows you your controller inputs.

However, [GC_Controller](https://github.com/PeterHo8888/GC_Controller) along with [NintendoSpy](https://github.com/jaburns/NintendoSpy) would require two Arduinos.

There's no reason to do this, so instead GC_Controller outputs `(char*)&controller.getReport();`, and this software will read it and display it.

No auto-detection of the correct serial port is present, so the user needs to set it in [SerialComm.java](https://github.com/PeterHo8888/GC_Viewer/blob/master/src/com/peter/SerialComm.java).
