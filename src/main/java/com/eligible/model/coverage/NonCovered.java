package com.eligible.model.coverage;

import com.eligible.model.EligibleObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = false)
// TODO check similarity with {@link FinancialFlow}
public class NonCovered extends EligibleObject {
    String type;
    String typeLabel;
    String timePeriod;
    String timePeriodLabel;
    String level;
    String network;
    String pos;
    String posLabel;
    Boolean authorizationRequired;
    List<ContactDetail> contactDetails;
    Dates dates;
    List<String> comments;
}
