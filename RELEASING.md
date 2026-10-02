# Releasing

This QBit uses the CircleCI configuration in `.circleci/config.yml` and the pinned `kingsrook/qqq-orb` to build, test and publish Maven artifacts. The current configuration uses orb **0.6.8**. Review the configuration and version inputs before a release; branch names and tags select different jobs.

## Current pipeline

| Trigger | Workflow | Behavior |
|---|---|---|
| Feature or main branch push | `test_only` | Builds and verifies; does not publish |
| Develop branch push | `publish_snapshot` | Verifies and publishes a snapshot |
| `release/*` branch push | `publish_release_candidate` | Verifies and publishes an RC |
| `hotfix/*` branch push | `publish_hotfix_release` | Selects the hotfix publisher |
| Tag matching `/v.*/` | `publish_release` | Selects the release publisher |

The stable release publisher ignores all branch pushes. Merging a release branch into main alone neither publishes a stable artifact nor creates a release tag. Integration branches matching the configuration's exclusion are also excluded from `test_only`.

The existing release-tag filter is broad: an RC-shaped tag can also match `/v.*/`. Do not infer the publication mode or artifact version from a tag label alone. Review the selected workflow, orb branch type and calculated Maven version before creating any RC or stable tag.

## Prerequisites

- The CircleCI project and existing `qqq-maven-registry-credentials` context must be configured for the intended publisher.
- Required checks and PR reviews must pass on the exact source to be released.
- The release owner must approve the intended immutable coordinates and confirm that they have not already been published.

Keep registry and signing credentials in the existing CI context. Do not add them to source or release instructions.

## Version inputs

The POM's `<revision>` is currently `0.5.0-SNAPSHOT`. The producer's `VERSION` constant reports the same current source version through QBit metadata. When changing the source version in a future reviewed update, keep these values aligned; there is no generated version mechanism.

The orb calculates the Maven publication version from the checked-out branch or tag, the POM revision and Git history. The snapshot, RC, hotfix and stable paths have different rules. Inspect the actual calculated value before accepting publication; these instructions do not promise a fresh immutable version for every push. The runtime metadata constant is not rewritten automatically by the orb.

## Release sequence

1. Integrate reviewed changes into develop. A develop merge selects snapshot publication; it is not merely a test run.
2. Validate the intended QQQ/dependency combination and approve a candidate version before creating or pushing a release branch. A `release/*` push selects RC publication.
3. Review the exact candidate source, test results and published artifact coordinates before preparing a stable release.
4. Merge approved source to main through normal protection requirements. This runs the main test workflow without publishing a stable artifact.
5. After separate release approval, create the intended stable tag on the reviewed source. Confirm that the selected release job and calculated Maven version match the approved immutable coordinates before accepting the publication.
6. Record the exact source commit, tag, successful publisher job and public artifacts in the release tracking issue. Reconcile main and develop through reviewed changes as needed.

Hotfix branches also select a publisher on push. Plan their version and integration back into the active development line before using that path. Do not use a branch push or a tag as a harmless way to test these release instructions.
