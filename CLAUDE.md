# qbit-customizable-table-views

## Knowledge base

A reviewed dossier for this repo lives in the second-brain vault:

- Hub / start here: `R:/Git.Local/KofTwentyTwo/second-brain/knowledge/qqq/qqq-hub.md`
- This repo's dossier: `R:/Git.Local/KofTwentyTwo/second-brain/knowledge/qqq/repos/qbit-customizable-table-views.md`
- QBit mechanics refresher: `R:/Git.Local/KofTwentyTwo/second-brain/knowledge/qqq/architecture/metadata-model.md`

Reviewed at commit `6a0f548afa8e` (branch `develop`, 2026-05-21). If HEAD has moved
significantly past that commit, treat the dossier as potentially stale.

Notes for agents working here:

- The README describes a "saved views" API (`QSavedViewMetaData`, `saved_view` tables) that
  does NOT exist in this code — trust the source, not the README. CHANGELOG.md and
  CONTRIBUTING.md are copy-pasted from qbit-user-role-permissions.
- Licensing metadata is contradictory (LICENSE/NOTICE and pom `<licenses>` = Apache-2.0;
  file headers, pom header comment, checkstyle template = AGPL-3.0; README footer says
  proprietary).
- Default builds take the qqq version only from `qbit-build-parent` (2.0.0 = qqq 4.0.0);
  do not add an always-on `qqq-bom-pom` import (ADR-0007). To check another qqq line, run
  `mvn -B verify -Dqqq.version=4.1.0-SNAPSHOT`, which activates the opt-in
  `qqq-version-override` profile.
