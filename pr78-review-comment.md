Thanks for adding the Week 3 retrospective and LLM usage report. The content of `reports/week3/retrospective.md` and `reports/week3/llm-report.md` generally matches the Assignment 3 structure and is useful for the Week 3 report.

However, this PR currently includes many unrelated repository changes that are outside the stated documentation scope. The diff deletes or moves important files such as `CHANGELOG.md`, `docs/user-stories.md`, issue templates, Week 2 images, and a large part of the existing frontend structure. Because this PR is supposed to close documentation tasks #77 and #81, it should only include the Week 3 report documentation files.

Please clean up the branch so the PR contains only the intended documentation changes, mainly:

- `reports/week3/retrospective.md`
- `reports/week3/llm-report.md`

Also, please update the PR description:

- add a short description of what was added;
- describe completion criteria verification for #77 and #81;
- describe testing performed, for example Markdown review and link/check confirmation;
- keep the documentation checkbox selected.

After the unrelated file changes are removed, this should be good to review again.
