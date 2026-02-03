# Git Workflow Guide

## Initial Setup

Initialize repository:
```bash
git init
git config user.name "Your Name"
git config user.email "your.email@example.com"
```

## Branch Strategy

main branches:
- master - Production code
- feature - featurement integration

Feature branches:
- feature/exceptions
- feature/testing
- feature/refactor

## Basic Workflow

### Starting a Feature

Create branch from master:
```bash
git checkout master
git pull origin master
git checkout -b feature/exceptions
```

### Making Changes

Work and commit:
```bash
git add .
git commit -m "Add error handling for withdrawals"
git push origin feature/exceptions
```

### Completing a Feature

Merge back to master:
```bash
git checkout feature
git merge feature/exceptions
git push origin feature
git branch -d feature/exceptions
```

## Commit Message Format

Use clear, descriptive messages:

```
feat: Add deposit validation
fix: Correct overdraft calculation
docs: Update README with examples
test: Add withdraw method tests
```

Types:
- feat - New feature
- fix - Bug fix
- docs - Documentation
- test - Testing
- refactor - Code restructuring

## Common Commands

View status:
```bash
git status
git log --oneline
```

Undo changes:
```bash
git checkout -- filename.java
git reset --soft HEAD~1
```

Stash work:
```bash
git stash
git stash pop
```

## Branch Management

List branches:
```bash
git branch
git branch -a
```

Delete branch:
```bash
git branch -d branch-name
git push origin --delete branch-name
```

## Handling Conflicts

When conflicts occur:
1. Open conflicted files
2. Resolve conflicts manually
3. Add resolved files: git add filename.java
4. Complete merge: git commit

## Release Process

Create release:
```bash
git checkout feature
git checkout -b release/1.0.0
# Update version numbers
git commit -m "Bump version to 1.0.0"
git checkout master
git merge release/1.0.0
git tag -a v1.0.0 -m "Version 1.0.0"
git push origin master --tags
```

## Gitignore

Create .gitignore file:
```
*.class
*.log
.idea/
*.iml
.vscode/
bin/
build/
.DS_Store
```

## Daily Workflow

Morning:
```bash
git checkout feature
git pull origin feature
```

During work:
```bash
git add .
git commit -m "Description of changes"
git push origin feature/my-feature
```

End of day:
```bash
git push origin feature/my-feature
```

## Collaboration

Before pushing:
```bash
git pull origin feature
```

Update feature branch:
```bash
git checkout feature/my-feature
git merge feature
```

## Feature Branch Examples

Based on project requirements:

Error handling feature:
```bash
git checkout -b feature/exceptions
# Implement try-catch blocks and custom exceptions
git commit -m "feat: Add error handling for transactions"
```

Testing feature:
```bash
git checkout -b feature/testing
# Write JUnit tests for deposit, withdraw, transfer
git commit -m "test: Add JUnit tests for core methods"
```

## Best Practices

- Commit often with small changes
- Write clear commit messages
- Pull before pushing
- Delete merged branches
- Keep branches short-lived
- Test before committing

## Troubleshooting

Committed to wrong branch:
```bash
git reset HEAD~1
git stash
git checkout correct-branch
git stash pop
```

Need to update from feature:
```bash
git checkout feature/my-feature
git merge feature
```

## Version Tags

Create tag:
```bash
git tag -a v1.0.0 -m "Version 1.0.0"
git push origin --tags
```

List tags:
```bash
git tag
```

## Summary

Basic workflow:
1. Create feature branch
2. Make changes and commit
3. Push to remote
4. Merge to feature
5. Delete feature branch

This workflow supports the project's requirements for version control integration with feature branches and controlled merging.
