# spring-batch-extensions for CSV file format

Spring Batch extension provides compensation for some `Comma Separated Values (CSV)` errors like missing `escape and/or quote characters`.
The compensation rules do roughly follow the ideas of `Libre Office` module.

Implementation can be used for

     * Spring Batch 3
     * Java 5

## Example

    "begin;'abc' d 'ef';end"
    =>
    "begin;'abc'' d ''ef';end"

For a more complete list of test cases, please refer to unit tests in `FixingCsvRecordSeparatorPolicyTest`.


## Usage

Inject the `Separator Policy` implementation into `FlatFileItemReader` using the setter `setRecordSeparatorPolicy`.

    <bean class="org.springframework.batch.item.file.FlatFileItemReader">
        <property name="recordSeparatorPolicy">
            <bean class="de.mediait.batch.FixingCsvRecordSeparatorPolicy">
            </bean>
        </property>
        ...
    </bean>


## Problem discussion

A more detailed discussion about specific cases and the implementation used for `Libre Office`
can be found here:

     * https://bugs.freedesktop.org/show_bug.cgi?id=48621

