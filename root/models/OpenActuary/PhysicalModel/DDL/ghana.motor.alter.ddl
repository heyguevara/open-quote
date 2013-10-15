ALTER TABLE polPolicy alter column polCoInsuranceCover set default 0;
ALTER TABLE polPolicy alter column polFacultativeCover set default 0;
ALTER TABLE polPolicy alter column polQuotaShare set default 0;
ALTER TABLE polPolicy alter column polTax set default 0;
ALTER TABLE polPolicy alter column polGrossPremiumAmount set default 0;
ALTER TABLE driDriver ADD UNIQUE (driID);
ALTER TABLE accAccident ADD UNIQUE (accID);
ALTER TABLE theTheftFire ADD UNIQUE (theID);
ALTER TABLE addAddress ADD UNIQUE (addID);
ALTER TABLE claClaim ADD UNIQUE (claID);
ALTER TABLE modENModel ADD UNIQUE (modID);
ALTER TABLE makENMake ADD UNIQUE (makID);
ALTER TABLE vehVehicle ADD UNIQUE (vehID);
ALTER TABLE polPolicy ADD UNIQUE (polID);

