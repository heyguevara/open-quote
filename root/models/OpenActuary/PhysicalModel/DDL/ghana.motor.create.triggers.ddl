CREATE TRIGGER pol-ins-bfr-uuid BEFORE INSERT ON polPolicy FOR EACH ROW set NEW.polID=UUID();
CREATE TRIGGER clm-ins-bfr-uuid BEFORE INSERT ON clmClaim FOR EACH ROW set NEW.clmID=UUID();
CREATE TRIGGER acc-ins-bfr-uuid BEFORE INSERT ON accAccident FOR EACH ROW set NEW.accID=UUID();
CREATE TRIGGER the-ins-bfr-uuid BEFORE INSERT ON theTheftFire FOR EACH ROW set NEW.theID=UUID();
CREATE TRIGGER veh-ins-bfr-uuid BEFORE INSERT ON vehVehicle FOR EACH ROW set NEW.vehID=UUID();
CREATE TRIGGER dri-ins-bfr-uuid BEFORE INSERT ON driDriver FOR EACH ROW set NEW.driID=UUID();
CREATE TRIGGER add-ins-bfr-uuid BEFORE INSERT ON addAddress FOR EACH ROW set NEW.addID=UUID();
