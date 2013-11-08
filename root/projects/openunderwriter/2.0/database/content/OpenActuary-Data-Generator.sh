START=814696123    # 26/10/1995 - no records will be generated before this date
END=1382776150     # 26/10/2013 - no records will be generated after this date
ONE_YEAR=31557600  # seconds in a year
ONE_DAY=86400      # seconds in a day

POLICY_LIST=./.policy_list

COVER_TYPES=("COMPREHENSIVE" "THIRD PARTY FIRE AND THEFT" "THIRD PARTY ONLY")
INSURED_TYPES=("CORPORATE" "PERSONAL") 
INSURANCE_RATES=("17.5" "15" "12.5" "10")
BODY_TYPES=("4X4" "CONVERTIBLE" "COUPE" "ESTATE" "HATCHBACK" "SALOON" "SPORTS")
PARKING_TYPES=("GARAGE" "OFF ROAD" "ON ROAD") 
RISK_CODES=("AMBULANCE / HEARSE-Z405" "ART/TANKER Z301" "CLASS II" "CLASS III" "GWI- CLASS I" "HIRING CARS-Z405" "MAXI BUS-Z405" "MINI BUS-Z405" "TAXI-Z405" "X.1" "X.4" "Y.3" "Z.300 ABOVE 3000 CC" "Z.300 BELOW 3000 CC" "Z.301 ABOVE 3000 CC" "Z.301 BELOW 3000 CC" "Z.802 ROAD" "Z.802 SITE")
COMMERCIAL_VEHICLE=("true" "false" "false" "false" "false")
AIRBAG=("true" "false" "false" "false" "false")
ENGINE_SIZE=(900 1000 1100 1300 1500 1800 2000 2200 2500 2600 3000 3500 4000 4500 5000) 
CLAIM_TYPES=("DAMAGE TO VEHICLE BY FIRE" "ECOWAS BROWN CARD" "MOTOR FIRE AND THEFT" "MOTOR OWN DAMAGE" "THIRD PARTY INJURY" "THIRD PARTY PROPERTY DAMAGE")


RANGE=$[ $END - $START ]
MULTIPLIER=$[ $RANGE / 32767 ]

NEW_BUSINESS=1
RENEWAL=2
CLAIM=3
CANCELLATION=4
ENDORSEMENT=5

function output_policy() 
{
    echo "update polPolicy set polReplaced=NOW() where polReplaced is null and polExternalSystemID='$POLICY_NUMBER';"
    echo "insert into polPolicy ("
    echo "    polCoverTypeIDcov,"
    echo "    polInsuredTypeIDins,"
    echo "    polReasonIDrea,"
    echo "    polExternalSystemID,"
    echo "    polInception,"
    echo "    polExpiry,"
    echo "    polLeadInsurer,"
    echo "    polTax,"
    echo "    polUWYear,"
    echo "    polGrossPremiumAmount,"
    echo "    polGrossPremiumIDccy"
    echo ")"
    echo "values ("
    echo "    '${COVER_TYPES[POLICY_NUMBER%3]}', "
    echo "    '${INSURED_TYPES[POLICY_NUMBER%2]}', "
    echo "    '$EVENT', "
    echo "    '$POLICY_NUMBER', "
    echo "    '$(date -ju -r $INCEPT_DATE '+%Y-%m-%d')', "
    echo "    '$(date -ju -r $EXPIRY_DATE '+%Y-%m-%d')', "
    echo "    true, "
    echo "    ${INSURANCE_RATES[POLICY_NUMBER%4]}, "
    echo "    $(date -ju -r $INCEPT_DATE '+%Y'), "
    echo "    $[300 + POLICY_NUMBER % 2000], "
    echo "    'GHS'"
    echo ");"
}

function output_vehicle() {
    YEAR_OF_MANUFACTURE=$(date -ju -r $FIRST_RECORD_DATE '+%Y')
    YEAR_OF_MANUFACTURE=$[YEAR_OF_MANUFACTURE-POLICY_NUMBER%20]

    ANNUAL_MILEAGE=$[2000+(POLICY_NUMBER%5000)]
    MILEAGE=$(date -ju -r $EVENT_DATE '+%Y')
    MILEAGE=$[(MILEAGE - YEAR_OF_MANUFACTURE) * ANNUAL_MILEAGE]

    SUM_INSURED=$[700+(POLICY_NUMBER%48000)]

    echo "insert into vehVehicle ("
    echo "    vehPolicyIDpol, "
	echo "    vehBodyIDbod, "
	echo "    vehParkedIDpar, "
	echo "    vehRiskCodeIDrsk, "
	echo "    vehYearOfManufacture, "
	echo "    vehCommercialVehicle, "
	echo "    vehDriverAirBagFitted, "
	echo "    vehEngineSizeCC, "
	echo "    vehMilage, "
	echo "    vehSumInsuredLocalAmount, "
	echo "    vehSumInsuredLocalIDccy "
    echo ")"
	echo "values ("
	echo "    (select polID from polPolicy where polExternalSystemID=$POLICY_NUMBER and polReplaced is null),"
    echo "    '${BODY_TYPES[POLICY_NUMBER%${#BODY_TYPES[@]}]}',"
    echo "    '${PARKING_TYPES[POLICY_NUMBER%${#PARKING_TYPES[@]}]}',"
    echo "    '${RISK_CODES[POLICY_NUMBER%${#RISK_CODES[@]}]}',"
    echo "    $YEAR_OF_MANUFACTURE,"
    echo "    ${COMMERCIAL_VEHICLE[POLICY_NUMBER%${#COMMERCIAL_VEHICLE[@]}]},"
    echo "    ${AIRBAG[POLICY_NUMBER%${#AIRBAG[@]}]},"
    echo "    ${ENGINE_SIZE[POLICY_NUMBER%${#ENGINE_SIZE[@]}]},"
    echo "    $MILEAGE,"
    echo "    $SUM_INSURED,"
    echo "    'GHS'"
	echo ");";
}

function output_claim() {
    SEL=$[RANDOM%100]
    [ $SEL -ge  0 -a $SEL -lt  65 ] && CLAIM_MAX=2000 
    [ $SEL -ge 65 -a $SEL -lt  99 ] && CLAIM_MAX=10000 
    [ $SEL -ge 99 -a $SEL -lt 100 ] && CLAIM_MAX=100000
    
    INCIDENT_OCCURED=$[INCEPT_DATE + ((RANDOM%364)*ONE_DAY)]
    
	echo "insert into clmClaim ("
	echo "     clmPolicyIDpol,"
	echo "     clmVehicleIDveh,"
	echo "     clmClaimTypeIDcla,"
	echo "     clmAmountPaidAmount,"
	echo "     clmAmountPaidIDccy,"
	echo "     clmIncidentOccured"
	echo " )"
	echo " values ("
	echo "     (select polID from polPolicy where polExternalSystemID='$POLICY_NUMBER' and polReplaced is null),"
	echo "     (select vehID from vehVehicle join polPolicy on vehPolicyIDpol=polID where polExternalSystemID='$POLICY_NUMBER' and polReplaced is null),"
	echo "     '${CLAIM_TYPES[POLICY_NUMBER%${#CLAIM_TYPES[@]}]}',"
	echo "     $[200+(RANDOM%$CLAIM_MAX)],"
	echo "     'GHS',"
	echo "     '$(date -ju -r $INCIDENT_OCCURED '+%Y-%m-%d')'"
	echo " );"
}

function output_record()
{
    output_policy
    output_vehicle
}

function new_business() 
{
    EVENT="NEW BUSINESS"
    output_record
}

function renewal() 
{
    if [ $EXPIRY_DATE -lt $END ]; then
        EVENT="RENEWAL"
        INCEPT_DATE=$[INCEPT_DATE+ONE_YEAR]
        EXPIRY_DATE=$[EXPIRY_DATE+ONE_YEAR]
        output_record
    fi
}

function claim() 
{
    EVENT="CLAIM"
    output_record
    output_claim
}

function cancellation() 
{
    EVENT="CANCELLATION"
    output_record
}

function endorsement() 
{
    EVENT="ENDORSEMENT"
    output_record
}

function generate_policy_number() 
{
    while [ 1 ]; do
        POLICY_NUMBER=$[ 10000 + RANDOM ]
        [ `grep $POLICY_NUMBER $POLICY_LIST > /dev/null` ] || break
    done
    echo $POLICY_NUMBER >> $POLICY_LIST
}

rm $POLICY_LIST 2>/dev/null
touch $POLICY_LIST

for i in {1..10000}; do
    generate_policy_number
    INCEPT_DATE=$[ RANDOM * $MULTIPLIER + $START ]
    EXPIRY_DATE=$[ INCEPT_DATE + ONE_YEAR ]
    
    EVENT_DATE=$INCEPT_DATE
    FIRST_RECORD_DATE=$INCEPT_DATE
    
    [ $INCEPT_DATE -gt $[ START_DATE + ONE_YEAR ] ] && new_business

    while [ 1 ]; do
        OPERATION=$[ 1 + RANDOM % 5 ]

        CHANCE=$[ RANDOM % 100 ]
        
        [ $OPERATION = $CLAIM -a $CHANCE -gt 80 ] &&  claim;
        [ $OPERATION = $CANCELLATION -a $CHANCE -gt 75 ] &&  cancellation && break;
        [ $OPERATION = $ENDORSEMENT -a $CHANCE -gt 50 ] &&  endorsement;
        [ $OPERATION = $RENEWAL -a $CHANCE -gt 25 ] && renewal
        [ $OPERATION = $RENEWAL -a $CHANCE -lt 25 ] && break
    done
        
done

