package ic.doc.camera;

public class Camera implements WriteListener {

  private final Sensor sensor;
  private final MemoryCard memoryCard;
  private boolean powerOn;
  private boolean writing;

  public Camera(Sensor sensor1, MemoryCard memoryCard1) {
    this.sensor = sensor1;
    this.memoryCard = memoryCard1;
    powerOn = false;
    writing = false;
  }

  public void pressShutter() {

    if (powerOn) {
      writing = true;
      memoryCard.write(sensor.readData());
    }
  }

  public void powerOn() {
    sensor.powerUp();
    powerOn = true;
  }

  public void powerOff() {

    if (!writing) {
      sensor.powerDown();
      powerOn = false;
    }
  }

  @Override
  public void writeComplete() {
    writing = false;
  }
}

