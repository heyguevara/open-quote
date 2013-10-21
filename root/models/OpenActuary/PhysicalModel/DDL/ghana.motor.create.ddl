CREATE TABLE verVersion (
  verOverall    varchar(16) DEFAULT '0.0.0' comment 'Overall version of the published schema', 
  verSchema     int(10) DEFAULT 0 NOT NULL comment 'this is the schema version', 
  verAlteration int(10) DEFAULT 0 NOT NULL comment 'this is the most resent alteration that have been applied to the Schema', 
  verStaticData int(10) DEFAULT 0 NOT NULL comment 'This is the most recent version that applies the schema/alteration') comment='With this table we will track versions of Schema, alterations and static data';
CREATE TABLE occENOccupation (
  name      varchar(32) NOT NULL UNIQUE, 
  validFrom date, 
  validTo   date) comment='Occupation Type';
CREATE TABLE natENNationality (
  name      varchar(32) NOT NULL UNIQUE, 
  validFrom date, 
  validTo   date) comment='Nationality Type';
CREATE TABLE empENEmploymentType (
  name      varchar(32) NOT NULL UNIQUE, 
  validFrom date, 
  validTo   date) comment='Employment Type';
CREATE TABLE couENCountry (
  name      varchar(3) NOT NULL UNIQUE comment 'ISO 3 character name', 
  country   varchar(32) UNIQUE comment 'Full Country name', 
  validFrom date, 
  validTo   date) comment='Country Type';
CREATE TABLE driDriver (
  driID                                int(10) NOT NULL UNIQUE, 
  driAverageAnnualMilage               int(10) DEFAULT 0 comment 'Average annual mileage', 
  driLicenceFirstIssued                date comment 'Date licence first issued', 
  driMainDriver                        tinyint(1) comment 'Is this the main driver', 
  driPassedDrivingTest                 date comment 'Date driving test passed', 
  driVehicleOwner                      tinyint(1) comment 'Is this the vehicle owner', 
  driYearsLicenceHeld                  int(2) comment 'How many years has the licence been held', 
  driDateOfBirth                       date comment 'Date of Birth', 
  driDefectiveHearing                  tinyint(1) comment 'Does driver have defective hearing', 
  driDefectiveVision                   tinyint(1) comment 'Does driver have defective vision', 
  driMale                              tinyint(1) comment 'Is driver male', 
  driLastCriminalOffence               date comment 'Date of last criminal offence', 
  driLastDrivingOffence                date comment 'Date of last driving offence', 
  driLastMotorRelatedAccident          date comment 'Date of last motor related accident', 
  driLastMotorRelatedClaim             date comment 'Date of last motor related claim', 
  driMotorInsuranceCancelledByInsurer  tinyint(1) comment 'Previously has motor insurance been cancelled by insurer', 
  driMotorInsuranceConditionsAdded     tinyint(1) comment 'Previously have condition been added by insurer to the motor insurance', 
  driMotorInsuranceRefusedOnRenewal    tinyint(1) comment 'Previously has motor insurance been refused on renewal by insurer', 
  driNumberOfCriminalOffences          int(2) comment 'Number of criminal offences', 
  driNumberOfDrivingOffenceConvictions int(2) comment 'Number of driving offence convictions', 
  driNumberOfMotorRelatedAccidents     int(2) comment 'Number of motor related accidents', 
  driNumberOfMotorRelatedClaims        int(2) comment 'Number of motor related claims', 
  driPreviouslyDeclineMotorInsurance   tinyint(1) comment 'Has been previously declined motor insurance', 
  driPreviousNCD                       tinyint(1) comment 'Previous no claims discount', 
  driRequiredIncreasePremium           tinyint(1) comment 'Has there been the need to increase the premium', 
  driSufferedFits                      tinyint(1) comment 'Does the driver suffer from fits', 
  driCurrentLicenceHeld                tinyint(1) comment 'Does driver hold a current driving licence', 
  driLicencedIssuedIDcou               varchar(32) comment 'Country licence issued', 
  driNatioinalityIDnat                 varchar(32) comment 'Driver nationality', 
  driOccuptationIDocc                  varchar(32) comment 'Driver occupatioin', 
  driEmploymentStatusIDemp             varchar(32) comment 'Drivers employment status') comment='Driver Table';
CREATE TABLE accAccident (
  accID                             int(11) UNIQUE, 
  accCarryingGoods                  tinyint(1) comment 'Were good being carried at time of accident', 
  accDamageToThirdParty             tinyint(1) comment 'Was there damage to a third party', 
  accDriverAirbagDeployed           tinyint(1) comment 'Did the driver airbags deploy', 
  addDriverAtTimeOfAccidentIDdri    int(10) comment 'Who was the driver at the time of the accident', 
  accDriverLiable                   tinyint(1) comment 'Is the driver liable for the accident', 
  accInjuryToDriver                 tinyint(1) comment 'Was there injury to the driver', 
  accInjuryOtherVehicleDrivers      tinyint(1) comment 'Was there injury sustained by any other vehicles driver', 
  accInjuryToOtherVehiclePassengers tinyint(1) comment 'Was there any injury sustained by any of the other vehicles passengers', 
  accInjuryToPassengers             tinyint(1) comment 'Was there any injury sustained by the other vehicles passengers', 
  accInjuryToThirdParties           tinyint(1) comment 'Was there any injury sustained by any of the other third parties', 
  accInsuredDriving                 tinyint(1) comment 'Was a named insured from the policy driving', 
  accLightsOn                       tinyint(1) comment 'Were the lights on after light up time', 
  accNumberOfVehiclesInvolved       int(3) comment 'How many vehicles were involved in the accident', 
  accPassengerAirbagDeployed        tinyint(1) comment 'Did passenger airbag(s) deploy', 
  accPoliceProsecutingDriver        tinyint(1) comment 'Are the police prosecuting driver due to this accident', 
  accPoliceRecorded                 tinyint(1) comment 'Was this accident recorded by the police', 
  accPoliceWitness                  tinyint(1) comment 'Did the police witness the accident', 
  accSeatBeltsUsed                  tinyint(1) comment 'Were the seatbelts in use at the time of the accident.', 
  accIDclm                          int(11)) comment='Accident details';
CREATE TABLE theTheftFire (
  theID                        int(11) NOT NULL UNIQUE, 
  theAlarmOn                   tinyint(1) comment 'Was an alarm on at time of theft', 
  theAllWindowsAndDoorsSecured tinyint(1) comment 'Were all windows and doors in the vehicle secured', 
  theAnyToolsInVehicle         tinyint(1) comment 'Were any tools or equipment in vehicle at time of theft', 
  theKeysInVehicle             tinyint(1) comment 'Were ignition keys in vehicle', 
  theKeysStolenWithVehicle     tinyint(1) comment 'Were ignition keys stolen with vehicle', 
  theLockedInGarage            tinyint(1) comment 'Was vehicle in a locked garaged prior to theft', 
  theClaimIDclm                int(11), 
  theIDclm                     int(11)) comment='Third party fire and theft information';
CREATE TABLE addAddress (
  addID       int(11) NOT NULL UNIQUE, 
  addRoad     varchar(100), 
  addAreaCode varchar(50) comment 'Either Postal code or area') comment='Address information';
CREATE TABLE claENClaimType (
  name      varchar(32) NOT NULL UNIQUE, 
  validFrom date, 
  validTo   date) comment='Claim Type';
CREATE TABLE clmClaim (
  clmID                     int(11) UNIQUE, 
  clmlPolicyIDpol           int(11), 
  clmIDveh                  int(11), 
  clmIDcla                  varchar(32), 
  clmAmountPaidAmount       decimal(12, 2) comment 'amount paid in claim to insured', 
  clmAmountPaidIDccy        varchar(3), 
  clmLegalFeesAmount        decimal(12, 2) comment 'legal fees incurred by claim', 
  clmLegalFeesIDccy         varchar(3), 
  clmRecoveriesAmount       decimal(12, 2) comment 'total recoveries made against claim', 
  clmRecoveriesIDccy        varchar(3), 
  clmTotalLossIncuredAmount decimal(12, 2) comment 'total loss of claim = paid + legal -recoveries', 
  clmTotalLossIncurredIDccy varchar(3), 
  clmIncidentAddressIDadd   int(11) comment 'where incident occurred', 
  clmIncidentMileageAtTime  int(10) comment 'main vehicle''s mileage at the time of the incident', 
  clmIncidentOccured        date comment 'when incident occurred') comment='Claim Details';
CREATE TABLE weiENWeightType (
  name      varchar(3) NOT NULL UNIQUE, 
  validFrom date, 
  validTo   date) comment='Unit of Weight';
CREATE TABLE risENRiskCode (
  name      varchar(32) NOT NULL UNIQUE, 
  validFrom date, 
  validTo   date) comment='Vehicle Risk Code e.g. X.1 , X.4, TAXI-Z405, HIRING CARS Z405, MINIBUS,MAXIBUS, Y3 etc.';
CREATE TABLE mdfENModifications (
  name      varchar(32) NOT NULL UNIQUE, 
  validFrom date, 
  validTo   date) comment='Vehicle Modifications Type';
CREATE TABLE accENAccessories (
  name      varchar(32) NOT NULL UNIQUE, 
  validFrom date, 
  validTo   date) comment='Vehicle Accessories Type';
CREATE TABLE parENParked (
  name      varchar(32) NOT NULL UNIQUE, 
  validFrom date, 
  validTo   date) comment='Where Vehicle Parked Type';
CREATE TABLE souENSoundSystem (
  name      varchar(32) NOT NULL UNIQUE, 
  validFrom date, 
  validTo   date) comment='Vehicle Sound System Type';
CREATE TABLE ccyENCurrency (
  name        varchar(3) NOT NULL UNIQUE, 
  description int(11), 
  symbol      char(3), 
  validFrom   date, 
  validTo     date) comment='Currency Type';
CREATE TABLE reaENReason (
  name      varchar(32) NOT NULL UNIQUE, 
  validFrom date, 
  validTo   date) comment='Reason for uploading Policy Data';
CREATE TABLE insENInsuredType (
  name      varchar(32) NOT NULL UNIQUE, 
  validFrom date, 
  validTo   date) comment='Insured Type';
CREATE TABLE modENModel (
  name  varchar(32) NOT NULL, 
  modID int(11) NOT NULL UNIQUE) comment='Vehicle Model Type';
CREATE TABLE cascadeMakeModel (
  makID     int(11) NOT NULL, 
  modlID    int(11) NOT NULL, 
  validFrom date, 
  validTo   date) comment='Relationship between Vehicle Make and Model';
CREATE TABLE makENMake (
  name  varchar(32) NOT NULL UNIQUE, 
  makID int(11) NOT NULL UNIQUE) comment='Vehicle Make Type';
CREATE TABLE bodENBodyType (
  name      varchar(32) NOT NULL UNIQUE, 
  validFrom date, 
  validTo   date) comment='Vehicle Body Type';
CREATE TABLE vehVehicle (
  vehID                           int(11) NOT NULL UNIQUE comment 'Unique Identifier', 
  vehPolicyIDpol                  int(11), 
  vehIDmak                        varchar(32), 
  vehIDmod                        int(11), 
  vehIDbod                        varchar(32), 
  vehIDsou                        varchar(32), 
  vehIDpar                        varchar(32), 
  vehIDacc                        varchar(32), 
  vehIDmdf                        varchar(32), 
  vehIDrsk                        varchar(32), 
  vehAlarmFitted                  tinyint(1) comment 'Does vehicle have a car alarm fitted', 
  vehBoughtFromNew                tinyint(1) comment 'Was vehicle bought from new', 
  vehYearOfManufacture            int(4) comment 'Year of vehicle''s manufacture', 
  vehCapacitiyVolume              decimal(10, 3) comment 'Vehicle''s carrying capacity by volume', 
  vehCommercialVehicle            tinyint(1) comment 'Is this a commercial vehicle', 
  vehCompanyVehicle               tinyint(1) comment 'Vehicle is owned by a company', 
  vehCoverStart                   date comment 'When did the cover start', 
  vehCoverEnd                     date comment 'When does cover end', 
  vehDriverAirBagFitted           tinyint(1) comment 'Does the vehicle have a driver''s airbag fitted', 
  vehEngineSizeCC                 int(5) comment 'Vehicle Engine capacity (cc)', 
  vehMilage                       int(7) comment 'Vehicle''s current mileage', 
  vehPassengerAirbagFitted        tinyint(1) comment 'Does the vehicle have a passenger''s airbag fitted', 
  vehRentalVehicle                tinyint(1) comment 'Is vehicle a rental', 
  vehRighthandDrive               tinyint(1) comment 'Is the vehicle right-hand drive', 
  vehRoadWorthyCertificate        tinyint(1) comment 'Does the vehicle hold a current Road worthy certificate', 
  vehRoadWorthyCertificateExpiry  date comment 'Vehicle''s current road worthy certificate expiry date', 
  vehSeatingCapacity              int(3) comment 'vehicle seating capacity', 
  vehSubjectToLoan                tinyint(1) comment 'Is the vehicle currently subject to an outstanding loan agreement', 
  vehToolsStroredDayTime          tinyint(1) comment 'are tools stored in the vehicle during the daytime', 
  vehToolsStroredNightTime        tinyint(1) comment 'are tools stored in the vehicle during the nighttime', 
  vehTrackingSystemFitted         tinyint(1) comment 'is the vehicle fitted with a tracking system', 
  vehTrailer                      tinyint(1) comment 'is the vehicle used for towing a trailer', 
  vehUsageBusiness                tinyint(1) comment 'is the vehicle used for business', 
  vehUsageCarriageOfGoods         tinyint(1) comment 'is the vehicle used for the carriage of goods', 
  vehUsageSocialHome              tinyint(1) comment 'is the vehicle used for home or socially', 
  vehWhenIsuredTookPossession     date comment 'what date did the insure take possession of the vehicle', 
  vehSumInsuredLocalAmount        decimal(15, 2) comment 'vehicle sum insured amount in the local currency', 
  vehSumInsuredLocalIDccy         varchar(3), 
  vehSumInsuredForeignAmount      decimal(15, 2) comment 'vehicle sum insured amount in the foreign currency', 
  vehSumInsuredForeignIDccy       varchar(3), 
  vehRiskCurrencyAmount           decimal(15, 2) comment 'vehicle risk amount', 
  vehRiskCurrencyIDccy            varchar(3), 
  vehPremiumForeignAmount         decimal(15, 2) comment 'vehicle foreign premium amount in foreign currency', 
  vehPremiumForeignIDccy          varchar(3), 
  vehCurrentEstimateedValueAmount decimal(15, 2) comment 'vehicle''s current estimated value', 
  vehCurrentEstimatedValueIDccy   varchar(3), 
  vehPremiumLocalAmount           decimal(15, 2) comment 'vehicle premium in local currency', 
  vehPremiumLocalIDccy            varchar(3), 
  vehValueWhenNewAmount           decimal(15, 2) comment 'value of vehicle when new', 
  vehValueWhenNewIDccy            varchar(3), 
  vehUnladedWeightAmount          decimal(10, 2) comment 'vehicle''s unladed weight', 
  vehUnladedWeightIDwei           varchar(3), 
  vehMaxLoadWeightAmount          decimal(10, 2) comment 'vehicle''s maximum loading amount', 
  vehMaxLoadWeightIDwei           varchar(3), 
  vehNightTimeLocationIDadd       int(11), 
  vehDayTimeLocationIDadd         int(11)) comment='Vehicle Details';
CREATE TABLE covENCover (
  name      varchar(32) NOT NULL UNIQUE, 
  validFrom date, 
  validTo   date) comment='Cover type';
CREATE TABLE polPolicy (
  polID                 int(11) NOT NULL UNIQUE, 
  polCoverTypeIDcov     varchar(32) comment 'Policy cover type e.g Comprehensive', 
  poInsuredTypeIDins    varchar(32) comment 'Policy Insured Type e.g. Company, Private', 
  polReasonIDrea        varchar(32) comment 'Business Source e.g. Direct, Marketing, Broker, Agent, Reinsurance, Banc assurance) ', 
  polReplaced           date comment 'date policy record was replace on system, if null then this is the first instance', 
  polClientID           varchar(100) comment 'To be populated by the company uploading the data with a value that helps them relate this record back to their internal systems. This should not hold a value that would allow others (outside of the company supplying the record) to identify the policy. Specifically, this should not be populated with a policy number. This column is not expected to be unique in the staging or master databases. It is expected that records relating to the same policy will have the same polClientID.', 
  polSystemID           varchar(100) comment 'Generated by ARDR when records are added to the master database. This is generated using the database''s UUID function.', 
  polInception          date comment 'policy inception date', 
  polExpiry             date comment 'policy expiration date', 
  polLeadInsurer        tinyint(1) comment 'is this the lead insurer', 
  polCoInsuranceCover   decimal(9, 4) DEFAULT 0 comment 'percentage co - insured', 
  polFacultativeCover   decimal(9, 4) DEFAULT 0 comment 'percentage of facultative cover', 
  polQuotaShare         decimal(9, 4) DEFAULT 0 comment 'percentage of quote share', 
  polTax                decimal(9, 4) DEFAULT 0 comment 'percentage of Tax', 
  polUWYear             int(4) comment 'underwriting year or year of account', 
  polGrossPremiumAmount decimal(9, 2) DEFAULT 0 comment 'gross premium amount', 
  polGrossPremiumIDccy  varchar(3), 
  polDriverIDdri        int(10)) comment='Main Policy Document';
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle805580 (vehUnladedWeightIDwei), ADD CONSTRAINT FKvehVehicle805580 FOREIGN KEY (vehUnladedWeightIDwei) REFERENCES weiENWeightType (name);
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle434686 (vehMaxLoadWeightIDwei), ADD CONSTRAINT FKvehVehicle434686 FOREIGN KEY (vehMaxLoadWeightIDwei) REFERENCES weiENWeightType (name);
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle513882 (vehNightTimeLocationIDadd), ADD CONSTRAINT FKvehVehicle513882 FOREIGN KEY (vehNightTimeLocationIDadd) REFERENCES addAddress (addID);
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle483024 (vehDayTimeLocationIDadd), ADD CONSTRAINT FKvehVehicle483024 FOREIGN KEY (vehDayTimeLocationIDadd) REFERENCES addAddress (addID);
ALTER TABLE clmClaim ADD INDEX FKclmClaim730050 (clmlPolicyIDpol), ADD CONSTRAINT FKclmClaim730050 FOREIGN KEY (clmlPolicyIDpol) REFERENCES polPolicy (polID);
ALTER TABLE clmClaim ADD INDEX FKclmClaim693436 (clmIDveh), ADD CONSTRAINT FKclmClaim693436 FOREIGN KEY (clmIDveh) REFERENCES vehVehicle (vehID);
ALTER TABLE clmClaim ADD INDEX FKclmClaim54 (clmIDcla), ADD CONSTRAINT FKclmClaim54 FOREIGN KEY (clmIDcla) REFERENCES claENClaimType (name);
ALTER TABLE clmClaim ADD INDEX FKclmClaim47506 (clmAmountPaidIDccy), ADD CONSTRAINT FKclmClaim47506 FOREIGN KEY (clmAmountPaidIDccy) REFERENCES ccyENCurrency (name);
ALTER TABLE clmClaim ADD INDEX FKclmClaim200310 (clmLegalFeesIDccy), ADD CONSTRAINT FKclmClaim200310 FOREIGN KEY (clmLegalFeesIDccy) REFERENCES ccyENCurrency (name);
ALTER TABLE clmClaim ADD INDEX FKclmClaim541304 (clmRecoveriesIDccy), ADD CONSTRAINT FKclmClaim541304 FOREIGN KEY (clmRecoveriesIDccy) REFERENCES ccyENCurrency (name);
ALTER TABLE clmClaim ADD INDEX FKclmClaim659378 (clmTotalLossIncurredIDccy), ADD CONSTRAINT FKclmClaim659378 FOREIGN KEY (clmTotalLossIncurredIDccy) REFERENCES ccyENCurrency (name);
ALTER TABLE clmClaim ADD INDEX FKclmClaim289057 (clmIncidentAddressIDadd), ADD CONSTRAINT FKclmClaim289057 FOREIGN KEY (clmIncidentAddressIDadd) REFERENCES addAddress (addID);
ALTER TABLE polPolicy ADD INDEX FKpolPolicy716061 (polDriverIDdri), ADD CONSTRAINT FKpolPolicy716061 FOREIGN KEY (polDriverIDdri) REFERENCES driDriver (driID);
ALTER TABLE driDriver ADD INDEX FKdriDriver500451 (driLicencedIssuedIDcou), ADD CONSTRAINT FKdriDriver500451 FOREIGN KEY (driLicencedIssuedIDcou) REFERENCES couENCountry (country);
ALTER TABLE accAccident ADD INDEX FKaccAcciden279153 (addDriverAtTimeOfAccidentIDdri), ADD CONSTRAINT FKaccAcciden279153 FOREIGN KEY (addDriverAtTimeOfAccidentIDdri) REFERENCES driDriver (driID);
ALTER TABLE driDriver ADD INDEX FKdriDriver381963 (driEmploymentStatusIDemp), ADD CONSTRAINT FKdriDriver381963 FOREIGN KEY (driEmploymentStatusIDemp) REFERENCES empENEmploymentType (name);
ALTER TABLE driDriver ADD INDEX FKdriDriver213188 (driNatioinalityIDnat), ADD CONSTRAINT FKdriDriver213188 FOREIGN KEY (driNatioinalityIDnat) REFERENCES natENNationality (name);
ALTER TABLE driDriver ADD INDEX FKdriDriver657757 (driOccuptationIDocc), ADD CONSTRAINT FKdriDriver657757 FOREIGN KEY (driOccuptationIDocc) REFERENCES occENOccupation (name);
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle975646 (vehIDsou), ADD CONSTRAINT FKvehVehicle975646 FOREIGN KEY (vehIDsou) REFERENCES souENSoundSystem (name);
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle876353 (vehIDpar), ADD CONSTRAINT FKvehVehicle876353 FOREIGN KEY (vehIDpar) REFERENCES parENParked (name);
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle886475 (vehIDacc), ADD CONSTRAINT FKvehVehicle886475 FOREIGN KEY (vehIDacc) REFERENCES accENAccessories (name);
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle962787 (vehIDmdf), ADD CONSTRAINT FKvehVehicle962787 FOREIGN KEY (vehIDmdf) REFERENCES mdfENModifications (name);
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle435925 (vehIDrsk), ADD CONSTRAINT FKvehVehicle435925 FOREIGN KEY (vehIDrsk) REFERENCES risENRiskCode (name);
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle822388 (vehSumInsuredLocalIDccy), ADD CONSTRAINT FKvehVehicle822388 FOREIGN KEY (vehSumInsuredLocalIDccy) REFERENCES ccyENCurrency (name);
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle964444 (vehSumInsuredForeignIDccy), ADD CONSTRAINT FKvehVehicle964444 FOREIGN KEY (vehSumInsuredForeignIDccy) REFERENCES ccyENCurrency (name);
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle358880 (vehRiskCurrencyIDccy), ADD CONSTRAINT FKvehVehicle358880 FOREIGN KEY (vehRiskCurrencyIDccy) REFERENCES ccyENCurrency (name);
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle193849 (vehPremiumForeignIDccy), ADD CONSTRAINT FKvehVehicle193849 FOREIGN KEY (vehPremiumForeignIDccy) REFERENCES ccyENCurrency (name);
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle977480 (vehCurrentEstimatedValueIDccy), ADD CONSTRAINT FKvehVehicle977480 FOREIGN KEY (vehCurrentEstimatedValueIDccy) REFERENCES ccyENCurrency (name);
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle192992 (vehPremiumLocalIDccy), ADD CONSTRAINT FKvehVehicle192992 FOREIGN KEY (vehPremiumLocalIDccy) REFERENCES ccyENCurrency (name);
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle86368 (vehValueWhenNewIDccy), ADD CONSTRAINT FKvehVehicle86368 FOREIGN KEY (vehValueWhenNewIDccy) REFERENCES ccyENCurrency (name);
ALTER TABLE polPolicy ADD INDEX FKpolPolicy482946 (poInsuredTypeIDins), ADD CONSTRAINT FKpolPolicy482946 FOREIGN KEY (poInsuredTypeIDins) REFERENCES insENInsuredType (name);
ALTER TABLE polPolicy ADD INDEX FKpolPolicy389109 (polReasonIDrea), ADD CONSTRAINT FKpolPolicy389109 FOREIGN KEY (polReasonIDrea) REFERENCES reaENReason (name);
ALTER TABLE polPolicy ADD INDEX FKpolPolicy254915 (polGrossPremiumIDccy), ADD CONSTRAINT FKpolPolicy254915 FOREIGN KEY (polGrossPremiumIDccy) REFERENCES ccyENCurrency (name);
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle338565 (vehPolicyIDpol), ADD CONSTRAINT FKvehVehicle338565 FOREIGN KEY (vehPolicyIDpol) REFERENCES polPolicy (polID);
ALTER TABLE polPolicy ADD INDEX FKpolPolicy72147 (polCoverTypeIDcov), ADD CONSTRAINT FKpolPolicy72147 FOREIGN KEY (polCoverTypeIDcov) REFERENCES covENCover (name);
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle766678 (vehIDbod), ADD CONSTRAINT FKvehVehicle766678 FOREIGN KEY (vehIDbod) REFERENCES bodENBodyType (name);
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle769412 (vehIDmod), ADD CONSTRAINT FKvehVehicle769412 FOREIGN KEY (vehIDmod) REFERENCES modENModel (modID);
ALTER TABLE vehVehicle ADD INDEX FKvehVehicle764335 (vehIDmak), ADD CONSTRAINT FKvehVehicle764335 FOREIGN KEY (vehIDmak) REFERENCES makENMake (name);
ALTER TABLE cascadeMakeModel ADD INDEX FKcascadeMak336466 (makID), ADD CONSTRAINT FKcascadeMak336466 FOREIGN KEY (makID) REFERENCES makENMake (makID);
ALTER TABLE cascadeMakeModel ADD INDEX FKcascadeMak936409 (modlID), ADD CONSTRAINT FKcascadeMak936409 FOREIGN KEY (modlID) REFERENCES modENModel (modID);
ALTER TABLE accAccident ADD INDEX FKaccAcciden945564 (accIDclm), ADD CONSTRAINT FKaccAcciden945564 FOREIGN KEY (accIDclm) REFERENCES clmClaim (clmID);
ALTER TABLE theTheftFire ADD INDEX FKtheTheftFi656947 (theIDclm), ADD CONSTRAINT FKtheTheftFi656947 FOREIGN KEY (theIDclm) REFERENCES clmClaim (clmID);

