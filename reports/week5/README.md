# Week 5 Report — LAMBA Team 22

**Project:** LAMBA  
**Team:** Team 22  
**Assignment:** Assignment 5 / Sprint 3  
**Sprint dates:** 29.06–05.07  
**Delivered increment:** MVP v2  
**Report status:** Draft. The report already indexes completed Week 5 documentation work, but release, APK/product artifact, demo video, screenshots, total Story Points, latest CI run, and contribution traceability still need to be added before final submission.

## 1. Project overview

LAMBA is a mobile application for car owners. The product helps users maintain a digital car profile, record expenses and car-related events, interact with an AI assistant, and review car usage information such as expenses, mileage, fuel, and maintenance history.

Week 5 focused on delivering MVP v2 while improving the maintainability and explainability of the project through architecture documentation, ADRs, development-process documentation, hosted documentation, quality evidence, and Sprint Review/UAT artifacts.

## 2. Public project links

| Artifact | Link |
|---|---|
| Product repository | [LAMBA_Team-22](https://github.com/ernest0507/LAMBA_Team-22) |
| Product Backlog | [GitHub Project — Product Backlog](https://github.com/users/ernest0507/projects/2) |
| Sprint 3 / Week 5 Sprint Backlog | [GitHub Project — Week 5 Sprint](https://github.com/users/ernest0507/projects/7) |
| Sprint 3 milestone | [Milestone 3](https://github.com/ernest0507/LAMBA_Team-22/milestone/3) |
| Hosted documentation site | [LAMBA Maintained Documentation](https://ernest0507.github.io/LAMBA_Team-22/) |
| MVP v2 release | [v0.3.0 — MVP v2 Assignment 5 Sprint Increment](https://github.com/ernest0507/LAMBA_Team-22/releases/tag/v0.3.0) |
| Product access artifact / APK | [APK file](https://drive.google.com/file/d/1maubS49OS057Yf7YjCeC9te9LATVTk89/view?usp=drive_link) |
| Public sanitized demo video | [Demo video](https://drive.google.com/file/d/1lmrOBYkKaIFhFAt0llBoP0dPD6egNjc7/view?usp=drive_link) |

## 3. Sprint overview

### Sprint Goal

The Sprint Goal was to deliver MVP v2 by adding statistics screens and improving the product based on customer feedback, while also making the project easier to understand and maintain through architecture, quality, and process documentation.

The main product focus was:

- adding statistics screens for expenses, mileage, and fuel-related information;
- preparing backend support for the statistics window;
- improving frontend screens for statistics;
- preserving more assistant conversation context to improve AI assistant behavior;
- adding loading and successful-operation feedback screens;
- adding photo attachment support for breakdown and maintenance records;
- improving vehicle registration by allowing users to select car make/model from lists while still supporting manual entry;
- continuing architecture and quality documentation work for Assignment 5.

### Sprint dates

| Field | Value |
|---|---|
| Sprint | Sprint 3 / Week 5 |
| Start date | 29.06 |
| Finish date | 05.07 |
| MVP mapping | MVP v2 |
| Total Sprint size | 42 Story Points |

## 4. Delivered MVP v2 changes

The delivered MVP v2 increment included the following product and documentation changes:

| Area | Delivered or discussed change | Status |
|---|---|---|
| Statistics | Added statistics screens for expenses, mileage, and fuel-related information. | Delivered / shown during review |
| Backend support | Prepared backend-related work for the statistics window. | Delivered / in Sprint scope |
| AI assistant | Improved assistant context by preserving more conversation history. | Delivered / needs further UX polish |
| Loading and confirmation states | Added loading/successful-operation feedback requested by the customer. | Delivered |
| Photo attachments | Added photo attachment support for breakdown and maintenance record forms. | Delivered |
| Vehicle registration | Added car make/model selection lists with manual entry fallback. | Delivered |
| Timeline/history | Continued work on displaying fuel, mileage, repair, and record history. | Delivered / verified during UAT discussion |
| Architecture documentation | Documented static, dynamic, and deployment views. | Delivered |
| ADRs | Added/updated architecture decision records and linked them to quality reasoning. | Delivered / maintained docs |
| Development process | Added maintained development-process and configuration-management documentation. | Delivered |
| Hosted documentation | Added MkDocs/GitHub Pages support for a browsable maintained documentation site. | Delivered / deploy link above |

## 5. Customer feedback response table

| Feedback point | Resulting PBI or issue | Status | Response |
|---|---|---|---|
| The customer requested clearer feedback after user actions such as registration or record creation. | [#162](https://github.com/ernest0507/LAMBA_Team-22/issues/162) | Done | Added loading and successful-operation feedback screens. |
| The customer wanted statistics beyond only monetary expenses. | [#34](https://github.com/ernest0507/LAMBA_Team-22/issues/34), [#165](https://github.com/ernest0507/LAMBA_Team-22/issues/165), [#166](https://github.com/ernest0507/LAMBA_Team-22/issues/166), [#189](https://github.com/ernest0507/LAMBA_Team-22/issues/189), [#181](https://github.com/ernest0507/LAMBA_Team-22/issues/181) | Done | Added statistics views for expenses, mileage, and fuel/liters. |
| The customer requested photo attachment support for breakdown or maintenance records. | [#163](https://github.com/ernest0507/LAMBA_Team-22/issues/163), [#179](https://github.com/ernest0507/LAMBA_Team-22/issues/179), [#180](https://github.com/ernest0507/LAMBA_Team-22/issues/180) | Done | Added a photo attachment field to breakdown and maintenance record forms. |
| The customer wanted more useful detail in the timeline/history. | [#180](https://github.com/ernest0507/LAMBA_Team-22/issues/180), [#182](https://github.com/ernest0507/LAMBA_Team-22/issues/182) | Done / in progress | Improved record history display and continued timeline/detail work. |
| The customer wanted the AI assistant to use more context. | [#190](https://github.com/ernest0507/LAMBA_Team-22/issues/190), [#178](https://github.com/ernest0507/LAMBA_Team-22/issues/178) | Done / needs polish | Preserved more assistant conversation context, but user-facing responses still need to become more human-like. |
| Vehicle registration should be easier than free-text-only input. | [#164](https://github.com/ernest0507/LAMBA_Team-22/issues/164) | Done | Added lists for selecting car make/model, with manual entry when the option is not available. |
| Trip mode and automatic mileage tracking would be valuable. | Follow-up backlog item planned; related mileage work: [#178](https://github.com/ernest0507/LAMBA_Team-22/issues/178) | Deferred / research | The team discussed possible GPS-based tracking and a simpler manual start/end mileage option. The customer agreed to research feasibility and choose a realistic implementation path. |
| UI details such as car drawings, app icon, spacing, and inconsistent buttons need polish. | Follow-up UI polish backlog item planned | Deferred | The team identified this as follow-up UI polish for later work. |
| PDF upload, broad AI data analysis, voice interaction, and multi-user car ownership are lower priority. | Descoped from MVP v2; no implementation issue created for Week 5 | Deferred / descoped from MVP v2 | The team explained that these features are not realistic for the current MVP timeline. The customer agreed that focusing on MVP value is reasonable. |
| Achievements could improve engagement. | Follow-up backlog item planned; related statistics foundation: [#34](https://github.com/ernest0507/LAMBA_Team-22/issues/34) | Planned / concept validated | The team presented achievement categories linked to statistics, road situations, and breakdown/maintenance history. The customer reacted positively. |

## 6. UAT results summary

The Week 5 Sprint Review included customer-executed UAT for old and new scenarios. The exact private recording timecodes are kept for Moodle/private submission only.

| UAT area | Result | Notes |
|---|---|---|
| Registration / login feedback flow | Passed with comments / improved | The flow was revisited because earlier feedback noted that users did not receive enough feedback after continuing. The team demonstrated the updated path. |
| AI chat record creation | Passed with comments | The assistant flow worked, but the customer wanted more human-like confirmation and clearer feedback. |
| Statistics screens | Passed / direction accepted | The customer reviewed the statistics direction for expenses, mileage, and fuel/liters. |
| Timeline/history records | Passed / direction accepted | The customer saw record history containing fuel, mileage, and repair-related records. |

### Remaining product fixes from UAT

- Improve AI assistant response tone and confirmation messages.
- Keep polishing statistics and history integration with real backend data.
- Add or refine trip mode after estimating GPS/manual implementation options.
- Continue UI polish for icons, spacing, button consistency, and car visuals.

## 7. Sprint Review artifacts

| Artifact | Link |
|---|---|
| Sprint Review transcript | [reports/week5/sprint-review-transcript.md](sprint-review-transcript.md) |
| Sprint Review summary | [reports/week5/sprint-review-summary.md](sprint-review-summary.md) |
| Sprint Review recording | Private-only — submitted through Moodle |
| UAT recording/timecodes | Private-only — submitted through Moodle |

The public transcript and summary are sanitized. Private recording links and exact private timecodes are not committed to the public repository.

## 8. Maintained documentation

| Artifact | Link |
|---|---|
| Hosted documentation site | [LAMBA Maintained Documentation](https://ernest0507.github.io/LAMBA_Team-22/) |
| Roadmap | [docs/roadmap.md](../../docs/roadmap.md) |
| Definition of Done | [docs/definition-of-done.md](../../docs/definition-of-done.md) |
| Testing overview | [docs/testing.md](../../docs/testing.md) |
| Quality requirements | [docs/quality-requirements.md](../../docs/quality-requirements.md) |
| Quality requirement tests | [docs/quality-requirement-tests.md](../../docs/quality-requirement-tests.md) |
| User Acceptance Tests | [docs/user-acceptance-tests.md](../../docs/user-acceptance-tests.md) |
| Development process and configuration management | [docs/development-process.md](../../docs/development-process.md) |
| Architecture overview | [docs/architecture/README.md](../../docs/architecture/README.md) |

## 9. Architecture documentation

| Architecture artifact | Link |
|---|---|
| Architecture overview | [docs/architecture/README.md](../../docs/architecture/README.md) |
| Static view artifacts | [docs/architecture/static-view/](../../docs/architecture/static-view/) |
| Dynamic view artifacts | [docs/architecture/dynamic-view/](../../docs/architecture/dynamic-view/) |
| Deployment view artifacts | [docs/architecture/deployment-view/](../../docs/architecture/deployment-view/) |
| ADR directory | [docs/architecture/adr/](../../docs/architecture/adr/) |

### Architecture summary

The architecture documentation explains the current delivered structure of LAMBA through three views:

- the **static view** shows the main components and relationships, including the Android client, FastAPI backend, PostgreSQL database, Alembic migrations, and external AI provider;
- the **dynamic view** explains a non-trivial record creation flow with authentication and token validation;
- the **deployment view** explains how the mobile client, backend container, database, migrations, and AI provider interact at runtime.

This supports reasoning about maintainability, reliability, validation, data persistence, and external-service dependency handling.

### Quality requirements and architecture decisions

The Week 5 architecture work supports the maintained quality requirements by making the relevant system boundaries visible. Registration response time, AI provider failure handling, AI-extracted data validation, and database persistence depend on how the frontend, backend, database, and AI provider interact. ADRs document important architecture decisions and explain how those decisions support maintainability and quality requirements.

## 10. Testing and CI status

| Evidence | Link / status |
|---|---|
| Testing overview | [docs/testing.md](../../docs/testing.md) |
| Quality requirements | [docs/quality-requirements.md](../../docs/quality-requirements.md) |
| Quality requirement tests | [docs/quality-requirement-tests.md](../../docs/quality-requirement-tests.md) |
| CI pipeline | [GitHub Actions](https://github.com/ernest0507/LAMBA_Team-22/actions) |
| Latest protected-default-branch CI run | [Successful GitHub Actions run](https://github.com/ernest0507/LAMBA_Team-22/actions/runs/28746344932) |
| Link checker | Active in CI |
| Documentation publishing workflow | Active through `Publish Documentation` workflow |
| Backend tests | Active in CI where applicable |

## 11. Release and product access

| Artifact | Status |
|---|---|
| SemVer release mapped to MVP v2 | [v0.3.0 — MVP v2 Assignment 5 Sprint Increment](https://github.com/ernest0507/LAMBA_Team-22/releases/tag/v0.3.0) |
| CHANGELOG | [CHANGELOG.md](../../CHANGELOG.md) |
| Product access artifact / APK | [APK file](https://drive.google.com/file/d/1maubS49OS057Yf7YjCeC9te9LATVTk89/view?usp=drive_link) |
| Run/access instructions | Download the APK from the linked artifact and install it on an Android device or Android emulator. If Android blocks installation from unknown sources, allow installation for the file manager/browser used to open the APK. |
| Public sanitized demo video | [Demo video](https://drive.google.com/file/d/1lmrOBYkKaIFhFAt0llBoP0dPD6egNjc7/view?usp=drive_link) |

The expected reviewer path is to download the APK from the linked product access artifact and run it on an Android device or emulator. Exact private access instructions and credentials, if needed, will be provided only through Moodle.

## 12. Product screenshots

The following screenshots show the delivered MVP v2 product state and the main Week 5 product areas.

| Product screenshot | Path |
|---|---|
| Expense/history screen | `reports/week5/images/expense-history-screen.jpg` |
| Statistics screen for July 2026 | `reports/week5/images/statistics-month-july-2026.jpg` |
| Statistics screen for June 2026 | `reports/week5/images/statistics-month-june-2026.jpg` |
| Statistics screen for year 2026 | `reports/week5/images/statistics-year-2026.jpg` |
| AI assistant chat screen | `reports/week5/images/ai-assistant-chat-screen.jpg` |

## 13. Screenshots and visual evidence

Screenshots must be added to `reports/week5/images/` before final submission.

| Required screenshot | Status | Expected path |
|---|---|---|
| Sprint milestone | Added | `reports/week5/images/sprint-milestone.png` |
| Product/Sprint board or workflow view | Added | `reports/week5/images/sprint-board.png` |
| Latest protected-default-branch CI run | Added | `reports/week5/images/latest-ci-run.png` |
| SemVer MVP v2 release | Added | `reports/week5/images/release.png` |
| Example reviewed issue-linked PR | Added | `reports/week5/images/reviewed-pr.png` |
| Hosted documentation site | Added | `reports/week5/images/hosted-docs-site.png` |
| Product access artifact screenshot | Added | `reports/week5/images/product-access-artifact.png` |

## 14. Week 5 report artifacts

| Artifact | Link |
|---|---|
| Sprint Review summary | [reports/week5/sprint-review-summary.md](sprint-review-summary.md) |
| Sprint Review transcript | [reports/week5/sprint-review-transcript.md](sprint-review-transcript.md) |
| Retrospective | [reports/week5/retrospective.md](retrospective.md) |
| Reflection | [reports/week5/reflection.md](reflection.md) |
| LLM usage report | [reports/week5/llm-report.md](llm-report.md) |

## 15. Contribution traceability

The table below summarizes Week 5 contribution evidence based on merged issue-linked pull requests and reviewed changes.

| Team member | Week 5 contribution | Issues / PRs / evidence |
|---|---|---|
| Maya Gavrilova | Implemented backend support for car statistics and persisted assistant chat history. | [#191](https://github.com/ernest0507/LAMBA_Team-22/pull/191), [#192](https://github.com/ernest0507/LAMBA_Team-22/pull/192) |
| Gleb Demchin | Worked on backend/API and Android integration tasks: registration response time under load, statistics API connection, record photo flow, maintenance photo API, assistant mileage updates, and Android history compile fixes. | [#182](https://github.com/ernest0507/LAMBA_Team-22/pull/182), [#183](https://github.com/ernest0507/LAMBA_Team-22/pull/183), [#184](https://github.com/ernest0507/LAMBA_Team-22/pull/184), [#185](https://github.com/ernest0507/LAMBA_Team-22/pull/185), [#186](https://github.com/ernest0507/LAMBA_Team-22/pull/186), [#188](https://github.com/ernest0507/LAMBA_Team-22/pull/188) |
| Vladimir Germanov | Prepared Week 5 review/process documentation and hosted documentation site support. | [#198](https://github.com/ernest0507/LAMBA_Team-22/pull/198), [#200](https://github.com/ernest0507/LAMBA_Team-22/pull/200), [#209](https://github.com/ernest0507/LAMBA_Team-22/pull/209) |
| Ernest Kashapov | Implemented Assignment 5 documentation and quality work: architecture/ADR documentation, database workflow integration test, development-process documentation, UAT update, ADR/quality documentation, and architecture/quality index fixes. Also coordinated PO-side backlog and review alignment. | [#171](https://github.com/ernest0507/LAMBA_Team-22/pull/171), [#173](https://github.com/ernest0507/LAMBA_Team-22/pull/173), [#194](https://github.com/ernest0507/LAMBA_Team-22/pull/194), [#196](https://github.com/ernest0507/LAMBA_Team-22/pull/196), [#204](https://github.com/ernest0507/LAMBA_Team-22/pull/204), [#207](https://github.com/ernest0507/LAMBA_Team-22/pull/207) |
| Varvara Chizhikova | Implemented expenses/statistics screen work and updated the changelog for latest user-visible features. | [#169](https://github.com/ernest0507/LAMBA_Team-22/pull/169), [#205](https://github.com/ernest0507/LAMBA_Team-22/pull/205) |
| Ildar Faskhutdinov | Implemented frontend updates for image picker fields, car brand/model fields, and history card updates. | [#168](https://github.com/ernest0507/LAMBA_Team-22/pull/168), [#174](https://github.com/ernest0507/LAMBA_Team-22/pull/174) |

## 16. Current product status

MVP v2 is in progress as the next release-mapped increment after MVP v1. The Sprint Review validated the direction of the statistics feature, AI assistant improvements, and backlog decisions. The customer accepted the general direction and gave follow-up feedback on AI assistant response quality, UI polish, trip mode, and achievements.

Before final Assignment 5 submission, the team still needs to:

- merge the final Week 5 README and screenshot updates into `main`;
- generate the final commit-hash permalink for this report;
- prepare the Moodle PDF private wrapper with recording links, private timecodes, and access instructions.

## 17. Next steps

1. Merge the final Week 5 README and screenshot updates into `main`.
2. Confirm that the hosted documentation site remains deployed and accessible.
3. After all PRs are merged, generate the final commit-hash permalinks for Moodle PDF submission.
4. Prepare the Moodle PDF private wrapper with the Sprint Review/UAT recording link, private timecodes, and access instructions.
