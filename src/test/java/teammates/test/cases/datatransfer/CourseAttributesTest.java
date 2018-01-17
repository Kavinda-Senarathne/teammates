package teammates.test.cases.datatransfer;

import static teammates.common.util.Const.EOL;

import java.util.Date;

import org.testng.annotations.Test;

import teammates.common.datatransfer.attributes.CourseAttributes;
import teammates.common.util.FieldValidator;
import teammates.common.util.StringHelper;
import teammates.test.cases.BaseTestCase;
import teammates.test.driver.StringHelperExtension;

/**
 * SUT: {@link CourseAttributes}.
 */
public class CourseAttributesTest extends BaseTestCase {

    private String validName = "validName";
    private String validId = "validId";
    private String validTimeZone = "validTimeZone";
    private Date validCreatedAt = new Date(98765);

    @Test
    public void testStandardBuilder() {
        CourseAttributes courseAttributes = CourseAttributes
                .builder(validId, validName, validTimeZone)
                .build();
        assertEquals(new Date(), courseAttributes.createdAt);
        assertEquals(validId, courseAttributes.getId());
        assertEquals(validName, courseAttributes.getName());
        assertEquals(validTimeZone, courseAttributes.getTimeZone());
    }

    @Test
    public void testBuilderWithCreatedAt() {
        CourseAttributes caWithCreatedAt = CourseAttributes
                .builder(validId, validName, validTimeZone)
                .withCreatedAt(validCreatedAt)
                .build();
        assertEquals(validId, caWithCreatedAt.getId());
        assertEquals(validName, caWithCreatedAt.getName());
        assertEquals(validTimeZone, caWithCreatedAt.getTimeZone());
        assertEquals(validCreatedAt, caWithCreatedAt.createdAt);
    }

    @Test
    public void testBuilderWithNullId() {
        try {
            CourseAttributes.builder(null, validName, validTimeZone)
                    .build();
            signalFailureToDetectException();
        } catch (AssertionError e) {
            assertEquals("Non-null value expected", e.getMessage());
        }
    }

    @Test
    public void testBuilderWithNullName() {
        try {
            CourseAttributes.builder(validId, null, validTimeZone)
                    .build();
            signalFailureToDetectException();
        } catch (AssertionError e) {
            assertEquals("Non-null value expected", e.getMessage());
        }
    }

    @Test
    public void testBuilderWithNullTimeZone() {
        try {
            CourseAttributes.builder(validId, validName, null)
                    .build();
            signalFailureToDetectException();
        } catch (AssertionError e) {
            assertEquals("Non-null value expected", e.getMessage());
        }
    }

    @Test
    public void testBuilderWithNullCreatedAt() {
        CourseAttributes courseAttributes = CourseAttributes
                .builder(validId, validName, validTimeZone)
                .withCreatedAt(null)
                .build();
        assertEquals(new Date(), courseAttributes.createdAt);
    }

    @Test
    public void testValidate() throws Exception {

        CourseAttributes validCourse = generateValidCourseAttributesObject();

        assertTrue("valid value", validCourse.isValid());

        String veryLongId = StringHelperExtension.generateStringOfLength(FieldValidator.COURSE_ID_MAX_LENGTH + 1);
        String emptyName = "";
        String invalidTimeZone = "InvalidTimeZone";
        CourseAttributes invalidCourse = CourseAttributes
                .builder(veryLongId, emptyName, invalidTimeZone)
                .build();

        assertFalse("invalid value", invalidCourse.isValid());
        String errorMessage =
                getPopulatedErrorMessage(
                    FieldValidator.COURSE_ID_ERROR_MESSAGE, invalidCourse.getId(),
                    FieldValidator.COURSE_ID_FIELD_NAME, FieldValidator.REASON_TOO_LONG,
                    FieldValidator.COURSE_ID_MAX_LENGTH) + EOL
                + getPopulatedEmptyStringErrorMessage(
                      FieldValidator.SIZE_CAPPED_NON_EMPTY_STRING_ERROR_MESSAGE_EMPTY_STRING,
                      FieldValidator.COURSE_NAME_FIELD_NAME, FieldValidator.COURSE_NAME_MAX_LENGTH) + EOL
                + getPopulatedErrorMessage(
                      FieldValidator.COURSE_TIME_ZONE_ERROR_MESSAGE, invalidCourse.getTimeZone(),
                      FieldValidator.COURSE_TIME_ZONE_FIELD_NAME, FieldValidator.REASON_UNAVAILABLE_AS_CHOICE);
        assertEquals("invalid value", errorMessage, StringHelper.toString(invalidCourse.getInvalidityInfo()));
    }

    @Test
    public void testGetValidityInfo() {
        //already tested in testValidate() above
    }

    @Test
    public void testIsValid() {
        //already tested in testValidate() above
    }

    @Test
    public void testToString() {
        CourseAttributes c = generateValidCourseAttributesObject();
        assertEquals("[CourseAttributes] id: valid-id-$_abc name: valid-name timeZone: UTC", c.toString());
    }

    private static CourseAttributes generateValidCourseAttributesObject() {
        return CourseAttributes.builder("valid-id-$_abc", "valid-name", "UTC").build();
    }

}
