# Releasing

This QBit uses the [qqq-orb](https://circleci.com/developer/orbs/orb/kingsrook/qqq-orb) CircleCI orb for automated builds, versioning, and publishing to Maven Central. Releases are triggered by branch conventions -- no manual version bumping or publishing is required.

## Prerequisites

- CircleCI project linked to this repository
- `qqq-maven-registry-credentials` context configured in CircleCI with:
  - `MAVEN_USERNAME` / `MAVEN_PASSWORD` (Sonatype Central Portal)
  - `GPG_PRIVATE_KEY_B64` / `GPG_KEYNAME` / `GPG_PASSPHRASE` (artifact signing)
  - `GITHUB_TOKEN` (GitHub Release creation)

## Branch Workflow

```
feature/my-work --> develop --> release/X.Y --> main
                                                 \--> hotfix/X.Y.Z
```

### Development (develop branch)

Every commit to `develop` publishes a **SNAPSHOT** to Maven Central.

- Version format: `X.Y.Z-SNAPSHOT`
- Consumers use this for integration testing against the latest build

```bash
# Normal development workflow
git checkout develop
git pull
git checkout -b feature/my-feature
# ... make changes, commit, push ...
# Open PR targeting develop
# Merge PR -> SNAPSHOT auto-publishes
```

### Release Candidate (release/* branches)

When `develop` is ready for release, create a release branch. Every commit to a `release/*` branch publishes a **Release Candidate** to Maven Central.

- Version format: `X.Y.Z-RC.1`, `X.Y.Z-RC.2`, etc. (auto-incremented per commit)
- Use this for final QA and stabilization

```bash
# Create a release branch from develop
git checkout develop
git pull
git checkout -b release/0.4
git push -u origin release/0.4
# RC.1 auto-publishes

# If bug fixes are needed during QA:
git commit -m "fix: resolve edge case in view sharing"
git push
# RC.2 auto-publishes (incremented automatically)
```

### Production Release (main branch)

Merge the release branch into `main` to publish a **stable release**.

- Version format: `X.Y.Z`
- Automatically creates a git tag (`vX.Y.Z`) and a GitHub Release
- Published to Maven Central as a final release artifact

```bash
# Merge release branch to main (via PR recommended)
git checkout main
git merge release/0.4
git push

# CI auto-creates tag v0.4.0, GitHub Release, publishes to Maven Central

# After release, merge main back to develop and bump the revision
git checkout develop
git merge main
# Update <revision> in pom.xml to next development version
git commit -m "chore: bump revision to 0.5.0"
git push
```

### Hotfix (hotfix/* branches)

For critical production fixes that cannot wait for the next release cycle.

- Version format: `X.Y.(Z+1)` (patch bump from current release)
- Automatically creates a git tag and GitHub Release

```bash
# Create hotfix branch from main
git checkout main
git pull
git checkout -b hotfix/0.4.1
# ... apply fix ...
git commit -m "fix: critical null check in view loading"
git push -u origin hotfix/0.4.1
# Patch release auto-publishes

# Merge hotfix back to develop
git checkout develop
git merge hotfix/0.4.1
git push
```

## CI/CD Configuration

The release pipeline is defined in `.circleci/config.yml` and uses the `qqq-orb` to handle build, test, version calculation, and publishing. The workflows are:

| Workflow | Trigger | Branch Type | Publishes |
|----------|---------|-------------|-----------|
| `test_only` | Push to feature/* | -- | Nothing (test only) |
| `publish_snapshot` | Push to develop | snapshot | SNAPSHOT to Maven Central |
| `publish_release_candidate` | Push to release/* | release_candidate | RC to Maven Central |
| `publish_release` | Push to main | release | Stable to Maven Central + GitHub Release |
| `publish_hotfix_release` | Push to hotfix/* | hotfix | Patch to Maven Central + GitHub Release |

## Version Management

The project version is controlled by the `<revision>` property in `pom.xml`. The CI pipeline reads and manipulates this value automatically based on branch type. You should only manually update `<revision>` when bumping the development version on `develop` after a release.
