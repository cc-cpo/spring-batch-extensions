# spring-batch-extensions for CSV file format

Spring Batch extension which contains a `Separator Policy` implementation, that provides compensation for common `Comma Separated Values (CSV)` errors like missing `escape or quote characters`.


## Example

    "begin;'abc' d 'ef';end"
    =>
    "begin;'abc'' d ''ef';end"



## Usage

Inject the `Separator Policy` implementation into `FlatFileItemReader` using the setter `setRecordSeparatorPolicy`.

    <bean class="org.springframework.batch.item.file.FlatFileItemReader">
        <property name="recordSeparatorPolicy">
            <bean class="de.mediait.batch.FixingCsvRecordSeparatorPolicy">
            </bean>
        </property>
        ...
    </bean>
        

