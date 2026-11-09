# Test Data

Login tests load their records from `login-data.local.json`. Create your local fixture
from the safe template:

```bash
cp test-data/login-data.json test-data/login-data.local.json
```

Replace the sample values with an authorized test account. The local fixture is
git-ignored so credentials stay out of source control. Add one JSON object per
data-driven test case:

```json
[
  {
    "username": "test.user@example.com",
    "password": "your-test-password"
  }
]
```
