/**
 * 
 */
package org.apache.velocity;

/**
 * @author mark
 *
 */
public class Car {

  private IDriver drive;
  
  
  /**
   * @param assign _drive to {@linkplain #drive}
   */
  public void setDrive(IDriver _drive) {
    this.drive = _drive;
  }

  /**
   * @param _drive
   */
  public Car(IDriver _drive) {
    super();
    this.drive = _drive;
  }

  /**
   * 
   */
  public Car() {
    // TODO Auto-generated constructor stub
  }

  public String getDriverName(){
    return this.drive.getName();
  }

  public String getDriverName(final IDriver _driver){
    return _driver.getName();
  }

  public String getDriverName(final ButtonImpl _driver){
    return _driver.getName();
  }
}
