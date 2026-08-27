package com.example

import com.example.domain.model.GradeCalculator
import org.junit.Assert.assertEquals
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testGradeCalculations() {
        val resultAplus = GradeCalculator.calculateGrade(95.0, 100.0)
        assertEquals("A+", resultAplus.grade)
        assertEquals(4.0, resultAplus.gpa, 0.01)
        assertEquals("Passed", resultAplus.status)

        val resultA = GradeCalculator.calculateGrade(82.0, 100.0)
        assertEquals("A", resultA.grade)
        assertEquals(3.7, resultA.gpa, 0.01)

        val resultB = GradeCalculator.calculateGrade(74.0, 100.0)
        assertEquals("B", resultB.grade)

        val resultC = GradeCalculator.calculateGrade(65.0, 100.0)
        assertEquals("C", resultC.grade)

        val resultD = GradeCalculator.calculateGrade(52.0, 100.0)
        assertEquals("D", resultD.grade)

        val resultF = GradeCalculator.calculateGrade(40.0, 100.0)
        assertEquals("F", resultF.grade)
        assertEquals("Failed", resultF.status)
    }
}
