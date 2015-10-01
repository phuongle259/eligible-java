package com.eligible;

import com.eligible.exception.AuthenticationException;
import com.eligible.exception.EligibleException;
import com.eligible.exception.InvalidRequestException;
import com.eligible.model.Claim;
import com.eligible.model.Coverage;
import com.eligible.model.Payer;
import com.eligible.net.APIResource;
import com.eligible.net.RequestOptions;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

import static com.eligible.util.TestUtil.resource;
import static org.junit.Assert.*;

/**
 * Main API Tests for running real Sandbox API calls.
 */
public class EligibleTest {
    static Map<String, Object> defaultCoverageParams = new HashMap<String, Object>();
    static Map<String, Object> defaultCoverageMedicareParams = new HashMap<String, Object>();
    static Map<String, Object> defaultCoverageCostEstimateParams = new HashMap<String, Object>();
    static Map<String, Object> defaultClaimParams = new HashMap<String, Object>();

    static String getUniqueEmail() {
        return String.format("test+bindings-%s@eligible.com", UUID.randomUUID().toString().substring(24));
    }

    static String getUniquePlanId() {
        return String.format("MY-J-PLAN-%s", UUID.randomUUID().toString().substring(24));
    }

    static Map<String, Object> getUniquePlanParams() {
        Map<String, Object> uniqueParams = new HashMap<String, Object>();
//        uniqueParams.putAll(defaultCardParams);
        uniqueParams.put("id", getUniquePlanId());
        return uniqueParams;
    }

    static Map<String, Object> getSubscriptionParams() throws EligibleException {
//        Plan plan = Plan.create(getUniquePlanParams());
        Map<String, Object> subscriptionParams = new HashMap<String, Object>();
//        subscriptionParams.put("plan", plan.getId());
        return subscriptionParams;
    }

    @Before
    public void before() {
    }

    @BeforeClass
    public static void setUp() throws Exception {
        Eligible.apiKey = "n5Cddnj2KST6YV9J2l2ztQQ2VrdPfzA4JPbn"; // eligible public test key
        Eligible.isTest = true;

        defaultCoverageParams.put("payer_id", "00001");
        defaultCoverageParams.put("provider_last_name", "Doe");
        defaultCoverageParams.put("provider_first_name", "John");
        defaultCoverageParams.put("provider_npi", "0123456789");
        defaultCoverageParams.put("member_id", "ZZZ445554301");
        defaultCoverageParams.put("member_first_name", "IDA");
        defaultCoverageParams.put("member_last_name", "FRANKLIN");
        defaultCoverageParams.put("member_dob", "1701-12-12");
        defaultCoverageParams.put("service_type", "30");

        defaultCoverageMedicareParams.put("provider_last_name", "Doe");
        defaultCoverageMedicareParams.put("provider_first_name", "John");
        defaultCoverageMedicareParams.put("provider_npi", "0123456789");
        defaultCoverageMedicareParams.put("member_id", "ZZZ445554301");
        defaultCoverageMedicareParams.put("member_first_name", "IDA");
        defaultCoverageMedicareParams.put("member_last_name", "FRANKLIN");
        defaultCoverageMedicareParams.put("member_dob", "1701-12-12");

        defaultCoverageCostEstimateParams.put("provider_price", "1500.50");
        defaultCoverageCostEstimateParams.put("service_type", "1");
        defaultCoverageCostEstimateParams.put("network", "IN");
        defaultCoverageCostEstimateParams.put("payer_id", "00001");
        defaultCoverageCostEstimateParams.put("provider_last_name", "Doe");
        defaultCoverageCostEstimateParams.put("provider_first_name", "John");
        defaultCoverageCostEstimateParams.put("provider_npi", "0123456789");
        defaultCoverageCostEstimateParams.put("member_id", "ZZZ445554301");
        defaultCoverageCostEstimateParams.put("member_first_name", "IDA");
        defaultCoverageCostEstimateParams.put("member_last_name", "FRANKLIN");
        defaultCoverageCostEstimateParams.put("member_dob", "1701-12-12");


        String claimReqJson = new Scanner(resource("claim_request.json", EligibleTest.class))
                .useDelimiter("\\A").next();
        defaultClaimParams = APIResource.GSON.fromJson(claimReqJson, Map.class);

    }

    @Test
    public void testAPIBase() throws EligibleException {
        assertEquals("https://gds.eligibleapi.com", Eligible.getApiBase());

        String test = "test";
        Eligible.overrideApiBase(test);
        assertEquals(test, Eligible.getApiBase());

        Eligible.overrideApiBase(Eligible.LIVE_API_BASE);
    }

    @Test
    public void testPayerRetrieve() throws EligibleException {
        Payer payer = Payer.retrieve("FLBLS");

        assertNotNull(payer);
        assertEquals("FLBLS", payer.getId());
        assertEquals("FLBLS", payer.getPayerId());
        assertTrue(payer.getNames().contains("Blue Cross Blue Shield of Florida"));
        assertTrue(payer.getNames().contains("Blue Cross of Florida"));
        assertTrue(payer.getNames().contains("Blue Shield of Florida"));
        assertTrue(payer.getNames().contains("Florida Blue Shield"));
        assertTrue(payer.getNames().contains("BCBS Florida"));
        assertNotNull(payer.getCreatedAt());
        assertNotNull(payer.getUpdatedAt());
        assertNotNull(payer.getSupportedEndpoints());
        assertFalse(payer.getSupportedEndpoints().isEmpty());
    }

    @Test
    public void testPayerRetrieveRawValues() throws EligibleException {
        Payer payer = Payer.retrieve("FLBLS");

        assertNotNull(payer.get("payer_id"));
        assertFalse(payer.getRawValues().isEmpty());
        assertEquals(payer.getPayerId(), payer.get("payer_id"));
        assertEquals(payer.getCreatedAt(), payer.get("created_at"));
        assertEquals(payer.getUpdatedAt(), payer.get("updated_at"));
        assertEquals(payer.getNames(), payer.get("names"));
        assertEquals(payer.getSupportedEndpoints().size(), ((List) payer.get("supported_endpoints")).size());
        assertFalse(payer.getSupportedEndpoints().get(0).getRawValues().isEmpty());
        assertEquals(payer.getSupportedEndpoints().get(0).getRawValues(), ((List) payer.get("supported_endpoints")).get(0));
    }

    @Test
    public void testPayerSearchOptionsAll() throws EligibleException {
        List<Payer.SearchOptions> searchOptionsList = Payer.searchOptions();
        assertNotNull(searchOptionsList);
        assertFalse(searchOptionsList.isEmpty());
        Payer.SearchOptions searchOptions = searchOptionsList.get(0);
        assertNotNull(searchOptions);
        assertNotNull(searchOptions.getPayerId());
        assertNotNull(searchOptions.getSearchOptions());
        assertFalse(searchOptions.getSearchOptions().isEmpty());
    }

    @Test
    public void testPayerSearchOptionsRetrieve() throws EligibleException {
        Payer.SearchOptions searchOptions = Payer.searchOptions("WYMCR");
        assertNotNull(searchOptions);
        assertEquals("WYMCR", searchOptions.getId());
        assertEquals("WYMCR", searchOptions.getPayerId());
        assertNotNull(searchOptions.getSearchOptions());
        assertFalse(searchOptions.getSearchOptions().isEmpty());
    }

    @Test
    public void testPayerRetrieveNullId() throws EligibleException {
        try {
            Payer.retrieve(null);
            fail("Retrieve null Payer didn't result in exception.");
        } catch (InvalidRequestException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testPayerRetrieveNullApiKey() throws EligibleException {
        try {
            Payer.retrieve(null, new RequestOptions(null, null, true));
            fail("Using null ApiKey didn't result in exception.");
        } catch (AuthenticationException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testPayerRetrieveInvalidApiKey() throws EligibleException {
        try {
            Payer.retrieve(null, new RequestOptions("invalid api key", null, true));
            fail("Using invalid ApiKey didn't result in exception.");
        } catch (AuthenticationException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testCoverageAll() throws EligibleException {
        Coverage coverage = Coverage.all(defaultCoverageParams);
        assertNotNull(coverage);
        assertNotNull(coverage.getEligibleId());
        assertNotNull(coverage.getId());
        assertNotNull(coverage.getDemographics());
        assertNotNull(coverage.getInsurance());
        assertNotNull(coverage.getPlan());
        assertNotNull(coverage.getServices());
        assertFalse(coverage.getServices().isEmpty());
        assertNotNull(coverage.getServices().get(0));
    }

    @Test
    public void testCoverageAllEmptyParams() throws EligibleException {
        try {
            Map<String, Object> invalidParams = new HashMap<String, Object>();
            Coverage coverage = Coverage.all(invalidParams);
            fail("API call didn't throw exception on empty params");
        } catch (InvalidRequestException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testCoverageAllInvalidParams() throws EligibleException {
        try {
            Map<String, Object> invalidParams = new HashMap<String, Object>(defaultCoverageParams);
            invalidParams.put("provider_npi", "ABC3456789"); // Non Numeric Characters in NPI
            Coverage coverage = Coverage.all(invalidParams);
            fail("API call didn't throw exception on empty params");
        } catch (InvalidRequestException e) {
            assertNotNull(e.getMessage());
        }
    }

    @Test
    public void testCoverageMedicare() throws EligibleException {
        Coverage.Medicare medicareCoverage = Coverage.medicare(defaultCoverageMedicareParams);
        assertNotNull(medicareCoverage);
        assertNotNull(medicareCoverage.getEligibleId());
        assertNotNull(medicareCoverage.getId());
        assertNotNull(medicareCoverage.getLastName());
        assertNotNull(medicareCoverage.getFirstName());
        assertNotNull(medicareCoverage.getMemberId());
        assertNotNull(medicareCoverage.getGroupId());
        assertNotNull(medicareCoverage.getGroupName());
        assertNotNull(medicareCoverage.getGender());
        assertNotNull(medicareCoverage.getPayerName());
        assertNotNull(medicareCoverage.getPlanNumber());
        assertNotNull(medicareCoverage.getEligibiltyDates());
        assertNotNull(medicareCoverage.getPlanTypes());
        assertFalse(medicareCoverage.getPlanTypes().isEmpty());
        assertNotNull(medicareCoverage.getPlanDetails());
        assertFalse(medicareCoverage.getPlanDetails().isEmpty());
    }

    @Test
    public void testCoverageCostEstimate() throws EligibleException {
        Coverage.CostEstimates costEstimates = Coverage.costEstimate(defaultCoverageCostEstimateParams);
        assertNotNull(costEstimates);
        assertNotNull(costEstimates.getEligibleId());
        assertNotNull(costEstimates.getId());
        assertNotNull(costEstimates.getDemographics());
        assertNotNull(costEstimates.getInsurance());
        assertNotNull(costEstimates.getPlan());
        assertNotNull(costEstimates.getServices());
        assertFalse(costEstimates.getServices().isEmpty());
        assertNotNull(costEstimates.getServices().get(0));
        assertNotNull(costEstimates.getCostEstimates());
        assertFalse(costEstimates.getCostEstimates().isEmpty());
        assertNotNull(costEstimates.getCostEstimates().get(0));
    }

    @Test
    public void testClaim() throws EligibleException {
        Claim claim = Claim.all(defaultClaimParams);
        assertNotNull(claim);
        assertNotNull(claim.getReferenceId());
        assertNotNull(claim.getId());
        assertNotNull(claim.getSuccess());
        assertNotNull(claim.getCreatedAt());
    }

}