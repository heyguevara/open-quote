CREATE TABLE verVersion (
  verOverall    varchar(16) DEFAULT '0.0.0' , 
  verSchema     int(10) DEFAULT 0 NOT NULL , 
  verAlteration int(10) DEFAULT 0 NOT NULL , 
  verStaticData int(10) DEFAULT 0 NOT NULL ) ;
CREATE TABLE occENOccupation (
  name      varchar(32) NOT NULL UNIQUE, 
  validFrom date, 
  validTo   date) ;
CREATE TABLE natENNationality (
  name      varchar(32) NOT NULL UNIQUE, 
  validFrom date, 
  validTo   date) ;
CREATE TABLE empENEmploymentType (
  name      varchar(32) NOT NULL UNIQUE, 
  validFrom date, 
  validTo   date) ;
CREATE TABLE couENCountry (
  name      varchar(3) NOT NULL UNIQUE , 
  country   varchar(32) UNIQUE , 
  validFrom date, 
  validTo   date) ;
CREATE TABLE driDriver (
  driID                                varchar(32) NOT NULL UNIQUE, 
  driAverageAnnualMilage               int(10) DEFAULT 0 , 
  driLicenceFirstIssued                date , 
  driMainDriver                        tinyint(1) , 
  driPassedDrivingTest                 date , 
  driVehicleOwner                      tinyint(1) , 
  driYearsLicenceHeld                  int(2) , 
  driDateOfBirth                       date , 
  driDefectiveHearing                  tinyint(1) , 
  driDefectiveVision                   tinyint(1) , 
  driMale                              tinyint(1) , 
  driLastCriminalOffence               date , 
  driLastDrivingOffence                date , 
  driLastMotorRelatedAccident          date , 
  driLastMotorRelatedClaim             date , 
  driMotorInsuranceCancelledByInsurer  tinyint(1) , 
  driMotorInsuranceConditionsAdded     tinyint(1) , 
  driMotorInsuranceRefusedOnRenewal    tinyint(1) , 
  driNumberOfCriminalOffences          int(2) , 
  driNumberOfDrivingOffenceConvictions int(2) , 
  driNumberOfMotorRelatedAccidents     int(2) , 
  driNumberOfMotorRelatedClaims        int(2) , 
  driPreviouslyDeclineMotorInsurance   tinyint(1) , 
  driPreviousNCD                       tinyint(1) , 
  driRequiredIncreasePremium           tinyint(1) , 
  driSufferedFits                      tinyint(1) , 
  driCurrentLicenceHeld                tinyint(1) , 
  driLicencedIssuedIDcou               varchar(32) , 
  driNatioinalityIDnat                 varchar(32) , 
  driOccuptationIDocc                  varchar(32) , 
  driEmploymentStatusIDemp             varchar(32) ) ;
CREATE TABLE accAccident (
  accID                             varchar(32) UNIQUE, 
  accCarryingGoods                  tinyint(1) , 
  accDamageToThirdParty             tinyint(1) , 
  accDriverAirbagDeployed           tinyint(1) , 
  addDriverAtTimeOfAccidentIDdri    varchar(32) , 
  accDriverLiable                   tinyint(1) , 
  accInjuryToDriver                 tinyint(1) , 
  accInjuryOtherVehicleDrivers      tinyint(1) , 
  accInjuryToOtherVehiclePassengers tinyint(1) , 
  accInjuryToPassengers             tinyint(1) , 
  accInjuryToThirdParties           tinyint(1) , 
  accInsuredDriving                 tinyint(1) , 
  accLightsOn                       tinyint(1) , 
  accNumberOfVehiclesInvolved       int(3) , 
  accPassengerAirbagDeployed        tinyint(1) , 
  accPoliceProsecutingDriver        tinyint(1) , 
  accPoliceRecorded                 tinyint(1) , 
  accPoliceWitness                  tinyint(1) , 
  accSeatBeltsUsed                  tinyint(1) , 
  accIDclm                          varchar(32)) ;
CREATE TABLE theTheftFire (
  theID                        varchar(32) NOT NULL UNIQUE, 
  theAlarmOn                   tinyint(1) , 
  theAllWindowsAndDoorsSecured tinyint(1) , 
  theAnyToolsInVehicle         tinyint(1) , 
  theKeysInVehicle             tinyint(1) , 
  theKeysStolenWithVehicle     tinyint(1) , 
  theLockedInGarage            tinyint(1) , 
  theClaimIDclm                varchar(32), 
  theIDclm                     varchar(32)) ;
CREATE TABLE addAddress (
  addID       varchar(32) NOT NULL UNIQUE, 
  addRoad     varchar(100), 
  addAreaCode varchar(50) ) ;
CREATE TABLE claENClaimType (
  name      varchar(32) NOT NULL UNIQUE, 
  validFrom date, 
  validTo   date) ;
CREATE TABLE clmClaim (
  clmID                     varchar(32) UNIQUE, 
  clmlPolicyIDpol           varchar(32), 
  clmIDveh                  varchar(32), 
  clmIDcla                  varchar(32), 
  clmAmountPaidAmount       decimal(12, 2) , 
  clmAmountPaidIDccy        varchar(3), 
  clmLegalFeesAmount        decimal(12, 2) , 
  clmLegalFeesIDccy         varchar(3), 
  clmRecoveriesAmount       decimal(12, 2) , 
  clmRecoveriesIDccy        varchar(3), 
  clmTotalLossIncuredAmount decimal(12, 2) , 
  clmTotalLossIncurredIDccy varchar(3), 
  clmIncidentAddressIDadd   varchar(32) , 
  clmIncidentMileageAtTime  int(10) , 
  clmIncidentOccured        date ) ;
CREATE TABLE weiENWeightType (
  name      varchar(3) NOT NULL UNIQUE, 
  validFrom date, 
  validTo   date) ;
CREATE TABLE risENRiskCode (
  name      varchar(32) NOT NULL UNIQUE, 
  validFrom date, 
  validTo   date) ;
CREATE TABLE mdfENModifications (
  name      varchar(32) NOT NULL UNIQUE, 
  validFrom date, 
  validTo   date) ;
CREATE TABLE accENAccessories (
  name      varchar(32) NOT NULL UNIQUE, 
  validFrom date, 
  validTo   date) ;
CREATE TABLE parENParked (
  name      varchar(32) NOT NULL UNIQUE, 
  validFrom date, 
  validTo   date) ;
CREATE TABLE souENSoundSystem (
  name      varchar(32) NOT NULL UNIQUE, 
  validFrom date, 
  validTo   date) ;
CREATE TABLE ccyENCurrency (
  name        varchar(3) NOT NULL UNIQUE, 
  description int(11), 
  symbol      char(3), 
  validFrom   date, 
  validTo     date) ;
CREATE TABLE reaENReason (
  name      varchar(32) NOT NULL UNIQUE, 
  validFrom date, 
  validTo   date) ;
CREATE TABLE insENInsuredType (
  name      varchar(32) NOT NULL UNIQUE, 
  validFrom date, 
  validTo   date) ;
CREATE TABLE modENModel (
  name  varchar(32) NOT NULL, 
  modID int(11) NOT NULL UNIQUE) ;
CREATE TABLE cascadeMakeModel (
  makID     int(11) NOT NULL, 
  modlID    int(11) NOT NULL, 
  validFrom date, 
  validTo   date) ;
CREATE TABLE makENMake (
  name  varchar(32) NOT NULL UNIQUE, 
  makID int(11) NOT NULL UNIQUE) ;
CREATE TABLE bodENBodyType (
  name      varchar(32) NOT NULL UNIQUE, 
  validFrom date, 
  validTo   date) ;
CREATE TABLE vehVehicle (
  vehID                           varchar(32) NOT NULL UNIQUE , 
  vehPolicyIDpol                  varchar(32), 
  vehIDmak                        varchar(32), 
  vehIDmod                        int(11), 
  vehIDbod                        varchar(32), 
  vehIDsou                        varchar(32), 
  vehIDpar                        varchar(32), 
  vehIDacc                        varchar(32), 
  vehIDmdf                        varchar(32), 
  vehIDrsk                        varchar(32), 
  vehAlarmFitted                  tinyint(1) , 
  vehBoughtFromNew                tinyint(1) , 
  vehYearOfManufacture            int(4) , 
  vehCapacitiyVolume              decimal(10, 3) , 
  vehCommercialVehicle            tinyint(1) , 
  vehCompanyVehicle               tinyint(1) , 
  vehCoverStart                   date , 
  vehCoverEnd                     date , 
  vehDriverAirBagFitted           tinyint(1) , 
  vehEngineSizeCC                 int(5) , 
  vehMilage                       int(7) , 
  vehPassengerAirbagFitted        tinyint(1) , 
  vehRentalVehicle                tinyint(1) , 
  vehRighthandDrive               tinyint(1) , 
  vehRoadWorthyCertificate        tinyint(1) , 
  vehRoadWorthyCertificateExpiry  date , 
  vehSeatingCapacity              int(3) , 
  vehSubjectToLoan                tinyint(1) , 
  vehToolsStroredDayTime          tinyint(1) , 
  vehToolsStroredNightTime        tinyint(1) , 
  vehTrackingSystemFitted         tinyint(1) , 
  vehTrailer                      tinyint(1) , 
  vehUsageBusiness                tinyint(1) , 
  vehUsageCarriageOfGoods         tinyint(1) , 
  vehUsageSocialHome              tinyint(1) , 
  vehWhenIsuredTookPossession     date , 
  vehSumInsuredLocalAmount        decimal(15, 2) , 
  vehSumInsuredLocalIDccy         varchar(3), 
  vehSumInsuredForeignAmount      decimal(15, 2) , 
  vehSumInsuredForeignIDccy       varchar(3), 
  vehRiskCurrencyAmount           decimal(15, 2) , 
  vehRiskCurrencyIDccy            varchar(3), 
  vehPremiumForeignAmount         decimal(15, 2) , 
  vehPremiumForeignIDccy          varchar(3), 
  vehCurrentEstimateedValueAmount decimal(15, 2) , 
  vehCurrentEstimatedValueIDccy   varchar(3), 
  vehPremiumLocalAmount           decimal(15, 2) , 
  vehPremiumLocalIDccy            varchar(3), 
  vehValueWhenNewAmount           decimal(15, 2) , 
  vehValueWhenNewIDccy            varchar(3), 
  vehUnladedWeightAmount          decimal(10, 2) , 
  vehUnladedWeightIDwei           varchar(3), 
  vehMaxLoadWeightAmount          decimal(10, 2) , 
  vehMaxLoadWeightIDwei           varchar(3), 
  vehNightTimeLocationIDadd       varchar(32), 
  vehDayTimeLocationIDadd         varchar(32)) ;
CREATE TABLE covENCover (
  name      varchar(32) NOT NULL UNIQUE, 
  validFrom date, 
  validTo   date) ;
CREATE TABLE polPolicy (
  polID                 varchar(32) NOT NULL UNIQUE, 
  polCoverTypeIDcov     varchar(32) , 
  poInsuredTypeIDins    varchar(32) , 
  polReasonIDrea        varchar(32) , 
  polReplaced           date , 
  polClientID           varchar(100) , 
  polSystemID           varchar(100) , 
  polInception          date , 
  polExpiry             date , 
  polLeadInsurer        tinyint(1) , 
  polCoInsuranceCover   decimal(9, 4) DEFAULT 0 , 
  polFacultativeCover   decimal(9, 4) DEFAULT 0 , 
  polQuotaShare         decimal(9, 4) DEFAULT 0 , 
  polTax                decimal(9, 4) DEFAULT 0 , 
  polUWYear             int(4) , 
  polGrossPremiumAmount decimal(9, 2) DEFAULT 0 , 
  polGrossPremiumIDccy  varchar(3), 
  polDriverIDdri        varchar(32)) ;
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
