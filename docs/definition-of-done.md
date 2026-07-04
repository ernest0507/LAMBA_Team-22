# Definition of Done

A Product Backlog Item may be marked as `Done` only when both conditions are satisfied:

- Its issue-specific acceptance criteria are satisfied.
- This team Definition of Done is satisfied.

A Product Backlog Item is `Done` only when:

- [ ] All issue acceptance criteria are verified.
- [ ] The work is linked to the related GitHub issue or PBI.
- [ ] The work is reviewed and approved by another team member.
- [ ] The issue-linked pull request is merged into the protected default branch.
- [ ] Relevant CI checks pass on the pull request and protected default branch.
- [ ] Relevant automated unit tests pass
- [ ] Relevant automated integration tests pass.
- [ ] Relevant automated quality requirement tests pass for affected quality requirements.
- [ ] Critical modules affected by the change keep at least 30% automated line coverage or have a documented TA-approved exception
- [ ] The selected additional QA check passes when applicable.
- [ ] Testing evidence is preserved in the pull request, CI run, test report, or linked documentation.
- [ ] Registration and login changes handle valid input, invalid input, and clear user-facing error messages.
- [ ] Vehicle digital twin changes correctly save and display car data.
- [ ] Maintenance history, expense, and timeline changes keep records consistent between the UI, backend, and database where applicable.
- [ ] AI chat changes return a useful response or fallback message and do not freeze or crash the application.
- [ ] For user stories, all linked supporting PBIs required to satisfy the story are completed, reviewed, merged, verified, and marked `Done`
- [ ] `docs/testing.md` is updated when tests, coverage, CI checks, QA checks, or testing evidence change.
- [ ] `docs/quality-requirements.md` is updated when quality requirements change
- [ ] `docs/quality-requirement-tests.md` is updated when automated QRTs change.
- [ ] `docs/user-acceptance-tests.md` is updated when UAT scenarios, execution results, customer comments, or resulting PBIs change.
- [ ] `README.md` is updated when setup, run, deployment, backend, Android, or access instructions change.
- [ ] `CHANGELOG.md` is updated for user-visible changes, or the pull request marks the changelog update as not applicable.
- [ ] Public repository artifacts do not include credentials, private customer data, recordings, unnecessary PII, or confidential materials
- [ ] Customer feedback or UAT findings that affect scope, quality, or usability are linked to a follow-up issue, PBI, roadmap item, or documented decision.
- [ ] Architecture documentation and related ADRs are updated when the change affects backend structure, persistence, deployment, integrations, critical modules, or major quality risks.
- [ ] Quality requirements added or changed for Assignment 5 and later work link to the related ADRs when applicable.
