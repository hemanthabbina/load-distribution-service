package com.seneca.test.load.distribution.service.dto.input.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import com.seneca.test.load.distribution.service.dto.input.InputWorkLoad;

@Component
public class InputWorkLoadValidator implements Validator {

	@Override
	public boolean supports(Class<?> inputClass) {
		return InputWorkLoad.class.equals(inputClass);
	}

	@Override
	public void validate(Object inputClass, Errors errors) {
		InputWorkLoad workLoad = (InputWorkLoad) inputClass;

		int totalWorkLoadPercent = workLoad.getDistribution().stream().mapToInt(x -> x.getDistributionPercent()).sum();
		if (totalWorkLoadPercent > 100) {
			errors.rejectValue("distribution", "More than 100",
					new Object[] { "'distribution' - Sum of all input percentages can't be more than 100" },
					"Sum of all input percentages can't be more than 100");
		}

	}

}
