In the commit messages use this message schema:

----
[WIP - feat] Replaced SQL Data with NoSQL data support

The feature of NoSQL data must be supported with DynamoDB local server launched at runtime by the application itself, to be done

BREAKING CHANGE

Added "wired online data connection" requirement, DynamoDB on AWS cloud is used for data retrieving.
Removed partially sql data support with Cayenne ORM, local SQL data to be mantained only for local processing and for possible offline consultation
----
Where:
WIP can be also REL standing for RELEASE when a commit can be included into a release.

BREAKING CHANGE when the commit breaks old features/processes

feat can be one of the following:
feat – a new feature is introduced with the changes
fix – a bug fix has occurred
chore – changes that do not relate to a fix or feature and don't modify src or test files (for example updating dependencies)
refactor – refactored code that neither fixes a bug nor adds a feature
docs – updates to documentation such as a the README or other markdown files
style – changes that do not affect the meaning of the code, likely related to code formatting such as white-space, missing semi-colons, and so on.
test – including new or correcting previous tests
perf – performance improvements
ci – continuous integration related
build – changes that affect the build system or external dependencies
revert – reverts a previous commit