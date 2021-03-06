package com.eligible.model.coverage;

import com.eligible.model.EligibleObject;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Getter
@EqualsAndHashCode(callSuper = false)
public class FinancialFlows extends EligibleObject {
    List<FinancialFlow> inNetwork;
    List<FinancialFlow> outNetwork;
}
