package com.healthycoderapp;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

class BMICalculatorTest {

	private String environment = "dev";
	
	@BeforeAll
	static void beforeAll() {
		System.err.println("Before all unit tests");
	}
	
	@AfterAll
	static void afterAll() {
		// Add code such as close database connection, etc.
		System.out.println("After all unit tests");
	}
	
	@DisplayName(">>>>>>>> Verify the return type of isDietRecommended() >>>>>>>>")
	@ParameterizedTest(name = "weight={0}, height = height={1}") // Where 0 represent 1st parameter in the test and 1 represents second parameter in the test
//	@ValueSource(doubles = {89.0, 95.0, 110.0})
//	@CsvSource(value = {"89.0, 1.72", "95.0,1.75", "110.0, 1.75"}) // Comma separated values
	@CsvFileSource(resources = "/diet-recommended-input-data.csv", numLinesToSkip = 1)
	void should_ReturnTure_When_DietRecommended(Double coderWeight, Double coderHeight) {
		// given 
		double weight = coderWeight;
		double height = coderHeight; 
		
		// when
		boolean recommended = BMICalculator.isDietRecommended(weight, height);
		// then
		assertTrue(recommended);
	}
	
	@Test
	void should_ReturnFalue_When_DietNotRecommended() {
		// given 
		double weight = 50.0;
		double height = 1.92; 
		
		// when
		boolean recommended = BMICalculator.isDietRecommended(weight, height);
		// then
		assertFalse(recommended);
	}
	
	@Test
	void should_ThrowArithmeticException_When_HeightIsZero() {
		// given 
		double weight = 50.0;
		double height = 0; 
		
		// when
		Executable executable = () -> BMICalculator.isDietRecommended(weight, height); // Lambda Expression
		
		
		// then
		assertThrows(ArithmeticException.class, executable);
	}

	@Test
	void should_returnCoderWithWorstBMI_WhenCoderListNotEmpty() {
		// given
		List<Coder> coders = new ArrayList<>();
		coders.add(new Coder(1.80, 60.0));
		coders.add(new Coder(1.82, 98.0));
		coders.add(new Coder(1.82, 64.7));

		// when
		Coder coderWorstBMI = BMICalculator.findCoderWithWorstBMI(coders);
		
		// then
		assertAll(
			() -> assertEquals(1.82, coderWorstBMI.getHeight()),
			() -> assertEquals(98.0, coderWorstBMI.getWeight())
		);
	}
	
	@Test
	void should_ReturnNullWorstBMICoder_When_CoderListIsEmpty() {
		// given
		List<Coder> coders = new ArrayList<>();

		// when
		Coder coderWorstBMI = BMICalculator.findCoderWithWorstBMI(coders);
		
		// then
		assertNull(coderWorstBMI);
	}
	
	@Test
	void should_ReturnCorrectBMIScoreArray_When_CoderListIsNotEmpty() {
		// given
		List<Coder> coders = new ArrayList<>();
		coders.add(new Coder(1.80, 60.0));
		coders.add(new Coder(1.82, 98.0));
		coders.add(new Coder(1.82, 64.7));
		double[] expected = {18.52, 29.59, 19.53};
		
		// when
		double[] actualBmiScores = BMICalculator.getBMIScores(coders);
		
		// then
		assertArrayEquals(expected, actualBmiScores);
	}

	@Test
	void should_ReturnCoderWithworstBMIIn1Ms_WhenCoderListHas10000Elements() {
		// given 
		assumeTrue(this.environment.equals("Prod"));
		List<Coder> coders = new ArrayList<>();
		for(int i = 0; i<10000 ; i++) {
			coders.add(new Coder(1.0 + i, 10.0 + i));
		}
		
		// when 
		Executable executable = () -> BMICalculator.findCoderWithWorstBMI(coders);
		
		// then 
		assertTimeout(Duration.ofMillis(5), executable);
	}
}
