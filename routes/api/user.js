const mongoose = require('mongoose');
const passport = require('passport');
const router = require('express').Router();
const auth = require('../auth');
const User = mongoose.model('User');

//POST new user route (optional, everyone has access)
router.post('/', auth.optional, (req, res, next) => {
  const { body: { user } } = req;

  if(!user.email) {
    return res.status(422).json({
      errors: {
        message: 'Email is required',
      },
    });
  }

  if(!user.password) {
    return res.status(422).json({
      errors: {
        message: 'Password is required',
      },
    });
  }

  User.findOne({ 'email': user.email })
    .exec((err, existing) => {
      if(!err && existing) {
        return res.status(422).json({
          errors: {
            message: 'User already exists',
          },
        });

      } else {
        const finalUser = new User(user);

        finalUser.setPassword(user.password);

        return finalUser.save()
          .then(() => res.json({ user: finalUser.toAuthJSON() }));
      }
    })

});

//POST login route (optional, everyone has access)
router.post('/login', auth.optional, (req, res, next) => {
  const { body: { user } } = req;

  if(!user.email) {
    return res.status(422).json({
      errors: {
        message: 'Email is required',
      },
    });
  }

  if(!user.password) {
    return res.status(422).json({
      errors: {
        message: 'Password is required',
      },
    });
  }

  return passport.authenticate('local', { session: false }, (err, passportUser, info) => {
    if(err) {
      console.log(err)
      return next(err);
    }
    
    console.log(err, passportUser, info)

    if(passportUser) {
      const user = passportUser;
      user.token = passportUser.generateJWT();

      return res.json({ user: user.toAuthJSON() });
    }

    return res.status(400).json(info);
  })(req, res, next);
});

//GET current route (required, only authenticated users have access)
router.get('/current', auth.required, (req, res, next) => {
  const { payload: { id } } = req;

  return User.findById(id)
    .then((user) => {
      if(!user) {
        return res.sendStatus(400);
      }

      return res.json({ user: user.toAuthJSON() });
    });
});

//GET settings route (required, only authenticated users have access)
router.get('/settings', auth.required, (req, res, next) => {
  const { payload: { id } } = req;

  return User.findById(id)
    .then((user) => {
      if(!user) {
        return res.sendStatus(400);
      }

      return res.json({ settings: user.getSettings() });
    });
});

//POST settings route (required, only authenticated users have access)
router.post('/settings', auth.required, (req, res, next) => {
  const { payload: { id }, body: { settings } } = req;
  return User.findById(id)
    .then((user) => {
      if(!user) {
        return res.sendStatus(400);
      }
      user.setSettings(settings);
      user.save();
      return res.json({ settings: user.getSettings() });
    });
});

module.exports = router;
