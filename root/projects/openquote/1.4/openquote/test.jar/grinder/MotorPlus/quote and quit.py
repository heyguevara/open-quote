# MotorPlus test script.
# This script uses an unauthenticated session to get a quote for MotorPlus. It then quits from the quote screen.

from net.grinder.script import Test
from net.grinder.script.Grinder import grinder
from net.grinder.plugin.http import HTTPPluginControl, HTTPRequest
from HTTPClient import NVPair
connectionDefaults = HTTPPluginControl.getConnectionDefaults()
httpUtilities = HTTPPluginControl.getHTTPUtilities()

# To use a proxy server, uncomment the next line and set the host and port.
# connectionDefaults.setProxyServer("localhost", 8001)

# These definitions at the top level of the file are evaluated once,
# when the worker process is started.

connectionDefaults.defaultHeaders = \
  ( NVPair('User-Agent', 'Mozilla/5.0 (Macintosh; U; PPC Mac OS X Mach-O; en-US; rv:1.8.1.2) Gecko/20070219 Firefox/2.0.0.2'),
    NVPair('Accept-Encoding', 'gzip,deflate'),
    NVPair('Accept-Language', 'en-us,en;q=0.5'),
    NVPair('Accept-Charset', 'ISO-8859-1,utf-8;q=0.7,*;q=0.7'), )

headers0= \
  ( NVPair('Accept', 'text/xml,application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5'), )

headers1= \
  ( NVPair('Accept', 'image/png,*/*;q=0.5'), )

url0 = 'http://localhost:8080'

# Create an HTTPRequest for each request, then replace the
# reference to the HTTPRequest with an instrumented version.
# You can access the unadorned instance using request101.__target__.
request101 = HTTPRequest(url=url0, headers=headers0)
#request101 = Test(101, 'GET MotorPlus').wrap(request101)

request201 = HTTPRequest(url=url0, headers=headers0)
#request201 = Test(201, 'POST QuoteWindow').wrap(request201)

request202 = HTTPRequest(url=url0, headers=headers0)
#request202 = Test(202, 'GET QuoteWindow').wrap(request202)

request301 = HTTPRequest(url=url0, headers=headers0)
#request301 = Test(301, 'POST QuoteWindow').wrap(request301)

request302 = HTTPRequest(url=url0, headers=headers0)
#request302 = Test(302, 'GET QuoteWindow').wrap(request302)

request401 = HTTPRequest(url=url0, headers=headers0)
#request401 = Test(401, 'POST QuoteWindow').wrap(request401)

request402 = HTTPRequest(url=url0, headers=headers0)
#request402 = Test(402, 'GET QuoteWindow').wrap(request402)

request501 = HTTPRequest(url=url0, headers=headers0)
#request501 = Test(501, 'POST QuoteWindow').wrap(request501)

request502 = HTTPRequest(url=url0, headers=headers0)
#request502 = Test(502, 'GET QuoteWindow').wrap(request502)

request601 = HTTPRequest(url=url0, headers=headers0)
#request601 = Test(601, 'POST QuoteWindow').wrap(request601)

request602 = HTTPRequest(url=url0, headers=headers0)
#request602 = Test(602, 'GET QuoteWindow').wrap(request602)

request701 = HTTPRequest(url=url0, headers=headers0)
#request701 = Test(701, 'POST QuoteWindow').wrap(request701)

request702 = HTTPRequest(url=url0, headers=headers0)
#request702 = Test(702, 'GET QuoteWindow').wrap(request702)

request801 = HTTPRequest(url=url0, headers=headers0)
#request801 = Test(801, 'POST QuoteWindow').wrap(request801)

request802 = HTTPRequest(url=url0, headers=headers0)
#request802 = Test(802, 'GET QuoteWindow').wrap(request802)

class TestRunner:
  """A TestRunner instance is created for each worker thread."""

  # A method for each recorded page.
  def page1(self):
    """GET MotorPlus (requests 101-102)."""
    result = request101.GET('/portal/portal/default/MotorPlus')

    return result

  def page2(self):
    """POST QuoteWindow (requests 201-204)."""
    self.token_action = \
      '1'
    
    # Expecting 302 'Moved Temporarily'
    result = request201.POST('/portal/portal/default/MotorPlus/QuoteWindow' +
      '?action=' +
      self.token_action,
      ( NVPair('op=Get A Quote:immediate=false', 'Get A Quote'), ),
      ( NVPair('Content-Type', 'application/x-www-form-urlencoded'), ))
    self.token_action = \
      httpUtilities.valueFromLocationURI('action') # '2'

    grinder.sleep(88)
    request202.GET('/portal/portal/default/MotorPlus/QuoteWindow' +
      '?action=' +
      self.token_action)

    return result

  def page3(self):
    """POST QuoteWindow (requests 301-304)."""
    self.token_action = \
      '1'
    
    # Expecting 302 'Moved Temporarily'
    result = request301.POST('/portal/portal/default/MotorPlus/QuoteWindow' +
      '?action=' +
      self.token_action,
      ( NVPair('title', 'Mr.'),
        NVPair('firstname', 'Richard'),
        NVPair('surname', 'Anderson'),
        NVPair('address1', 'Little Owls'),
        NVPair('address2', 'Huntington Road'),
        NVPair('town', 'Crowborough'),
        NVPair('county', 'East Sussex'),
        NVPair('postcode', 'TN6 2LJ'),
        NVPair('phone', '07711 611522'),
        NVPair('email', 'rand@shadows.demon.co.uk'),
        NVPair('op=Next:immediate=false', 'Next'), ),
      ( NVPair('Content-Type', 'application/x-www-form-urlencoded'), ))
    self.token_action = \
      httpUtilities.valueFromLocationURI('action') # '2'

    grinder.sleep(17)
    request302.GET('/portal/portal/default/MotorPlus/QuoteWindow' +
      '?action=' +
      self.token_action)

    return result

  def page4(self):
    """POST QuoteWindow (requests 401-404)."""
    self.token_action = \
      '1'
    
    # Expecting 302 'Moved Temporarily'
    result = request401.POST('/portal/portal/default/MotorPlus/QuoteWindow' +
      '?action=' +
      self.token_action,
      ( NVPair('/asset[assetTypeId=#VehicleAsset#][0]attribute[id=#make#]', 'AC'),
        NVPair('/asset[assetTypeId=#VehicleAsset#][0]attribute[id=#model#]', 'ACE'),
        NVPair('/asset[assetTypeId=#VehicleAsset#][0]attribute[id=#cc#]', '2000'),
        NVPair('/asset[assetTypeId=#VehicleAsset#][0]attribute[id=#bodytype#]', 'Salon'),
        NVPair('/asset[assetTypeId=#VehicleAsset#][0]attribute[id=#year#]', '2001'),
        NVPair('/asset[assetTypeId=#VehicleAsset#][0]attribute[id=#registration#]', 'LGO 920Y'),
        NVPair('/asset[assetTypeId=#VehicleAsset#][0]attribute[id=#datebought#]', '01/12/2002'),
        NVPair('/asset[assetTypeId=#VehicleAsset#][0]attribute[id=#cost#]', '5000'),
        NVPair('/asset[assetTypeId=#VehicleAsset#][0]attribute[id=#value#]', '5000'),
        NVPair('op=Next:immediate=false', 'Next'), ),
      ( NVPair('Content-Type', 'application/x-www-form-urlencoded'), ))
    self.token_action = \
      httpUtilities.valueFromLocationURI('action') # '2'

    grinder.sleep(21)
    request402.GET('/portal/portal/default/MotorPlus/QuoteWindow' +
      '?action=' +
      self.token_action)

    return result

  def page5(self):
    """POST QuoteWindow (requests 501-504)."""
    self.token_action = \
      '1'
    
    # Expecting 302 'Moved Temporarily'
    result = request501.POST('/portal/portal/default/MotorPlus/QuoteWindow' +
      '?action=' +
      self.token_action,
      ( NVPair('/asset[assetTypeId=#VehicleAsset#][0]attribute[id=#lefthanddrive#]', 'No'),
        NVPair('/asset[assetTypeId=#VehicleAsset#][0]attribute[id=#registeredinproposersname#]', 'Yes'),
        NVPair('/asset[assetTypeId=#VehicleAsset#][0]attribute[id=#ownedbyproposer#]', 'Yes'),
        NVPair('/asset[assetTypeId=#VehicleAsset#][0]attribute[id=#driventoandfromwork#]', 'Yes'),
        NVPair('/asset[assetTypeId=#VehicleAsset#][0]attribute[id=#modified#]', 'No'),
        NVPair('/asset[assetTypeId=#VehicleAsset#][0]attribute[id=#securitydevice#]', 'Yes'),
        NVPair('/asset[assetTypeId=#VehicleAsset#][0]attribute[id=#keptatproposersaddress#]', 'Yes'),
        NVPair('/asset[assetTypeId=#VehicleAsset#][0]attribute[id=#storedingarage#]', 'Yes'),
        NVPair('/asset[assetTypeId=#VehicleAsset#][0]attribute[id=#garageofsolidconstruction#]', 'Yes'),
        NVPair('op=Next:immediate=false', 'Next'), ),
      ( NVPair('Content-Type', 'application/x-www-form-urlencoded'), ))
    self.token_action = \
      httpUtilities.valueFromLocationURI('action') # '2'

    grinder.sleep(19)
    request502.GET('/portal/portal/default/MotorPlus/QuoteWindow' +
      '?action=' +
      self.token_action)

    return result

  def page6(self):
    """POST QuoteWindow (requests 601-604)."""
    self.token_action = \
      '1'
    
    # Expecting 302 'Moved Temporarily'
    result = request601.POST('/portal/portal/default/MotorPlus/QuoteWindow' +
      '?action=' +
      self.token_action,
      ( NVPair('/asset[assetTypeId=#DriverAsset#][0]attribute[id=#name#]', 'Richard Anderson'),
        NVPair('/asset[assetTypeId=#DriverAsset#][0]attribute[id=#dob#]', '08/12/1964'),
        NVPair('/asset[assetTypeId=#DriverAsset#][0]attribute[id=#sex#]', 'Male'),
        NVPair('/asset[assetTypeId=#DriverAsset#][0]attribute[id=#occupation#]', 'Accountant'),
        NVPair('/asset[assetTypeId=#DriverAsset#][0]attribute[id=#licenseType#]', 'Full'),
        NVPair('/asset[assetTypeId=#DriverAsset#][0]attribute[id=#yearsLicenseHeld#]', '20'),
        NVPair('op=Next:immediate=false', 'Next'), ),
      ( NVPair('Content-Type', 'application/x-www-form-urlencoded'), ))
    self.token_action = \
      httpUtilities.valueFromLocationURI('action') # '2'

    request602.GET('/portal/portal/default/MotorPlus/QuoteWindow' +
      '?action=' +
      self.token_action)

    return result

  def page7(self):
    """POST QuoteWindow (requests 701-704)."""
    self.token_action = \
      '1'
    
    # Expecting 302 'Moved Temporarily'
    result = request701.POST('/portal/portal/default/MotorPlus/QuoteWindow' +
      '?action=' +
      self.token_action,
      ( NVPair('/asset[id=#driversHistory#]/attribute[id=#refusedMotorInsurance#]', 'No'),
        NVPair('/asset[id=#driversHistory#]/attribute[id=#claimAccidentOrLoss#]', 'No'),
        NVPair('/asset[assetTypeId=#AccidentHistoryAsset#][0]attribute[id=#driver#]', '?'),
        NVPair('/asset[assetTypeId=#AccidentHistoryAsset#][0]attribute[id=#date#]', ''),
        NVPair('/asset[assetTypeId=#AccidentHistoryAsset#][0]attribute[id=#atFault#]', '?'),
        NVPair('/asset[assetTypeId=#AccidentHistoryAsset#][0]attribute[id=#value#]', ''),
        NVPair('/asset[id=#driversHistory#]/attribute[id=#theft#]', 'No'),
        NVPair('/asset[id=#driversHistory#]/attribute[id=#motorConviction#]', 'No'),
        NVPair('/asset[assetTypeId=#ConvictionHistoryAsset#][0]attribute[id=#driver#]', '?'),
        NVPair('/asset[assetTypeId=#ConvictionHistoryAsset#][0]attribute[id=#date#]', ''),
        NVPair('/asset[assetTypeId=#ConvictionHistoryAsset#][0]attribute[id=#type#]', '?'),
        NVPair('/asset[id=#driversHistory#]/attribute[id=#otherConviction#]', 'No'),
        NVPair('/asset[id=#driversHistory#]/attribute[id=#previouslyInsured#]', 'No'),
        NVPair('/asset[id=#driversHistory#]/attribute[id=#gapInCover#]', '?'),
        NVPair('/asset[id=#driversHistory#]/attribute[id=#previousInsurer#]', ''),
        NVPair('/asset[id=#driversHistory#]/attribute[id=#previousPolicyNumber#]', ''),
        NVPair('/asset[id=#driversHistory#]/attribute[id=#claimingNoClaimsBonus#]', 'No'),
        NVPair('op=Next:immediate=false', 'Next'), ),
      ( NVPair('Content-Type', 'application/x-www-form-urlencoded'), ))
    self.token_action = \
      httpUtilities.valueFromLocationURI('action') # '2'

    grinder.sleep(11)
    request702.GET('/portal/portal/default/MotorPlus/QuoteWindow' +
      '?action=' +
      self.token_action)

    return result

  def page8(self):
    """POST QuoteWindow (requests 801-804)."""
    self.token_action = \
      '1'
    
    # Expecting 302 'Moved Temporarily'
    result = request801.POST('/portal/portal/default/MotorPlus/QuoteWindow' +
      '?action=' +
      self.token_action,
      ( NVPair('op=Quit:immediate=true', 'Quit'), ),
      ( NVPair('Content-Type', 'application/x-www-form-urlencoded'), ))
    self.token_action = \
      httpUtilities.valueFromLocationURI('action') # '2'

    request802.GET('/portal/portal/default/MotorPlus/QuoteWindow' +
      '?action=' +
      self.token_action)

    return result

  def __call__(self):
    """This method is called for every run performed by the worker thread."""
    self.page1()      # GET MotorPlus (requests 101-102)

    grinder.sleep(3192)
    self.page2()      # POST QuoteWindow (requests 201-204)

    grinder.sleep(21462)
    self.page3()      # POST QuoteWindow (requests 301-304)

    grinder.sleep(20325)
    self.page4()      # POST QuoteWindow (requests 401-404)

    grinder.sleep(3452)
    self.page5()      # POST QuoteWindow (requests 501-504)

    grinder.sleep(8035)
    self.page6()      # POST QuoteWindow (requests 601-604)

    grinder.sleep(4085)
    self.page7()      # POST QuoteWindow (requests 701-704)

    grinder.sleep(3626)
    self.page8()      # POST QuoteWindow (requests 801-804)


def instrumentMethod(test, method_name, c=TestRunner):
  """Instrument a method with the given Test."""
  unadorned = getattr(c, method_name)
  import new
  method = new.instancemethod(test.wrap(unadorned), None, c)
  setattr(c, method_name, method)

# Replace each method with an instrumented version.
# You can call the unadorned method using self.page1.__target__().
instrumentMethod(Test(100, 'Page 1'), 'page1')
instrumentMethod(Test(200, 'Page 2'), 'page2')
instrumentMethod(Test(300, 'Page 3'), 'page3')
instrumentMethod(Test(400, 'Page 4'), 'page4')
instrumentMethod(Test(500, 'Page 5'), 'page5')
instrumentMethod(Test(600, 'Page 6'), 'page6')
instrumentMethod(Test(700, 'Page 7'), 'page7')
instrumentMethod(Test(800, 'Page 8'), 'page8')
