### Repository:
– Always use Dependency inversion for repo to easily swap implementations if needed
– Inject dispatchers, don’t use directly
– Use mappers to transform from Data model to Domain model
– Mappers: Do not provide defaults for Strings, hard to scale and
localise
– Errors and Result discussions

### UseCases:
– Have single responsibility
– Not all logic belongs to UseCases. Add Raw business logic instead of ui displaying logic
– Don’t use platform specific dependencies to UseCases, such as Patterns
– Do not return resources for the UI from UseCases, such as strings
(error messages for validators). / do not use context in UseCases
– By using Result type and Error interface, we can listen to changes and pass them to analytics
– Typealias long Results

### Presentation
– Errors specific to the use case