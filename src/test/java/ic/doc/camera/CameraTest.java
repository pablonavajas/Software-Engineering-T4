package ic.doc.camera;

import org.jmock.Expectations;
import org.jmock.integration.junit4.JUnitRuleMockery;
import org.junit.Rule;
import org.junit.Test;

public class CameraTest {

  @Rule
  public JUnitRuleMockery context = new JUnitRuleMockery();

  Sensor sensor = context.mock(Sensor.class);
  MemoryCard memoryCard = context.mock(MemoryCard.class);

  Camera cam = new Camera(sensor, memoryCard);

  @Test
  public void switchingTheCameraOnPowersUpTheSensor() {

    context.checking(new Expectations() {{
      exactly(1).of(sensor).powerUp();
    }});

    cam.powerOn();
  }

  @Test
  public void pressShutterWithCameraOnCopiesData() {

    byte[] data = {1};

    context.checking(new Expectations() {{
      exactly(1).of(sensor).powerUp();
      exactly(1).of(sensor).readData(); will(returnValue(data));
      exactly(1).of(memoryCard).write(data);
    }});

    cam.powerOn();
    cam.pressShutter();
  }

  @Test
  public void switchingTheCameraOffPowersDownTheSensor() {

    context.checking(new Expectations() {{
      exactly(1).of(sensor).powerDown();
    }});

    cam.powerOff();
  }

  @Test
  public void pressShutterWithCameraOffDoesNothing() {

    context.checking(new Expectations() {{
      never(sensor);
      never(memoryCard);
    }});

    cam.pressShutter();
  }

  @Test
  public void switchingCameraOffWhileCopyingDataDoesNothing() {

    byte[] data = {1};

    context.checking(new Expectations() {{
      exactly(1).of(sensor).powerUp();
      exactly(1).of(sensor).readData(); will(returnValue(data));
      exactly(1).of(memoryCard).write(data);
      never(sensor).powerDown();
    }});

    cam.powerOn();
    cam.pressShutter();
    cam.powerOff();
  }

  @Test
  public void switchingCameraOffWhileCopyingDataFinishedWorks() {

    byte[] data = {1};

    context.checking(new Expectations() {{
      exactly(1).of(sensor).powerUp();
      exactly(1).of(sensor).readData(); will(returnValue(data));
      exactly(1).of(memoryCard).write(data);
      exactly(1).of(sensor).powerDown();
    }});

    cam.powerOn();
    cam.pressShutter();
    cam.writeComplete();
    cam.powerOff();
  }

}
