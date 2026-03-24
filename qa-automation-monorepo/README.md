# QA Automation Monorepo

This repository hosts multiple API and UI automation frameworks in one place.

## Structure

- `api/restsharp-dotnet` - API automation with RestSharp (.NET)
- `api/restassured-java` - API automation with Rest Assured (Java)
- `ui/selenium-java` - UI automation with Selenium (Java)
- `ui/playwright-ts` - UI automation with Playwright (TypeScript)
- `shared/test-data` - Shared test data files
- `shared/utilities` - Shared utilities/scripts
- `shared/reports` - Consolidated report output
- `.github/workflows` - Per-framework CI workflows

## Getting Started

1. Copy or move your existing framework code into the matching folders.
2. Keep each framework self-contained with its own dependencies and configs.
3. Push this folder to a new GitHub repository.

## Suggested Branch Strategy

- `main` for stable framework code
- feature branches per framework, for example:
  - `feature/restsharp-auth-tests`
  - `feature/playwright-smoke-suite`

## CI Behavior

Each workflow uses path filters, so only the changed framework is built/tested.
