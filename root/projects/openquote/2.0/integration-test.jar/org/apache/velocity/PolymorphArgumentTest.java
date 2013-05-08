/**
 * 
 */
package org.apache.velocity;

import java.io.StringWriter;

import org.apache.velocity.app.Velocity;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.junit.Test;

/**
 * @author mark
 * 
 */
public class PolymorphArgumentTest {

  @Test
  public void testIncluded() {
    Velocity.init();

    VelocityContext context = new VelocityContext();

    /*
     * Task 1. create Car class an interface (IDriver) and and extension class (ButtonImpl): update the vm to call a method on
     * Car's driver's name that calls a method on the interface; create instance of Car and ButtonImpl in test case; run test.
     */
    context.put("car", new Car(new ButtonImpl()));

    Template template = null;

    try {
      template = Velocity.getTemplate("target/test/integration-test.jar/org/apache/velocity//templateInclude.vm");
    } catch (ResourceNotFoundException rnfe) {
      // couldn't find the template
    } catch (ParseErrorException pee) {
      // syntax error: problem parsing the template
    } catch (MethodInvocationException mie) {
      // something invoked in the template
      // threw an exception
    } catch (Exception e) {
      System.out.println(e.toString());
    }

    StringWriter sw = new StringWriter();

    template.merge(context, sw);
    System.out.println(sw);
  }

  @Test
  public void testExcluded() {
    Velocity.init();

    VelocityContext context = new VelocityContext();

    /*
     * Task 1. create Car class an interface (IDriver) and and extension class (ButtonImpl): update the vm to call a method on
     * Car's driver's name that calls a method on the interface; create instance of Car and ButtonImpl in test case; run test.
     */
    context.put("car", new Car());
    context.put("driver", new ButtonImpl());

    Template template = null;

    try {
      template = Velocity.getTemplate("target/test/integration-test.jar/org/apache/velocity/templateExclude.vm");
    } catch (ResourceNotFoundException rnfe) {
      // couldn't find the template
    } catch (ParseErrorException pee) {
      // syntax error: problem parsing the template
    } catch (MethodInvocationException mie) {
      // something invoked in the template
      // threw an exception
    } catch (Exception e) {
      System.out.println(e.toString());
    }

    StringWriter sw = new StringWriter();

    template.merge(context, sw);
    System.out.println(sw);
  }
}
