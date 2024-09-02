const jwt = require('jsonwebtoken');

app.post('/login', (req, res) => {
  const { username, password } = req.body;
  // Validate credentials (this is just an example)
  if (username === 'user' && password === 'pass') {
    const token = jwt.sign({
      iss: 'https://intproj23.sit.kmutt.ac.th/kk3/',
      iat: Math.floor(Date.now() / 1000),
      exp: Math.floor(Date.now() / 1000) + (30 * 60), // 30 minutes
      name: 'SOMCHAI JAIDEE',
      oid: '533c0096-a48c-4be1-8e42-769a9f05a725',
      email: 'somchai.jaidee@example.com',
      role: 'user'
    }, 'your-secret-key');
    res.json({ token });
  } else {
    res.status(401).send('Unauthorized');
  }
});