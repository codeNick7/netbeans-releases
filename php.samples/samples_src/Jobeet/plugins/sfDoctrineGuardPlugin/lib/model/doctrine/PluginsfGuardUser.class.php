<?php
/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright 2011 Oracle and/or its affiliates. All rights reserved.
 *
 * Oracle and Java are registered trademarks of Oracle and/or its affiliates.
 * Other names may be trademarks of their respective owners.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common
 * Development and Distribution License("CDDL") (collectively, the
 * "License"). You may not use this file except in compliance with the
 * License. You can obtain a copy of the License at
 * http://www.netbeans.org/cddl-gplv2.html
 * or nbbuild/licenses/CDDL-GPL-2-CP. See the License for the
 * specific language governing permissions and limitations under the
 * License.  When distributing the software, include this License Header
 * Notice in each file and include the License file at
 * nbbuild/licenses/CDDL-GPL-2-CP.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the GPL Version 2 section of the License file that
 * accompanied this code. If applicable, add the following below the
 * License Header, with the fields enclosed by brackets [] replaced by
 * your own identifying information:
 * "Portions Copyrighted [year] [name of copyright owner]"
 *
 * If you wish your version of this file to be governed by only the CDDL
 * or only the GPL Version 2, indicate your decision by adding
 * "[Contributor] elects to include this software in this distribution
 * under the [CDDL or GPL Version 2] license." If you do not indicate a
 * single choice of license, a recipient has the option to distribute
 * your version of this file under either the CDDL, the GPL Version 2 or
 * to extend the choice of license to its licensees as provided above.
 * However, if you add GPL Version 2 code and therefore, elected the GPL
 * Version 2 license, then the option applies only if the new code is
 * made subject to such option by the copyright holder.
 *
 * Contributor(s):
 *
 * Portions Copyrighted 2011 Sun Microsystems, Inc.
 */

/**
 * This class has been auto-generated by the Doctrine ORM Framework
 */
abstract class PluginsfGuardUser extends BasesfGuardUser
{
  protected
    $profile        = null,
    $groups         = null,
    $permissions    = null,
    $allPermissions = null;

  public function __toString()
  {
    return $this->getUsername();
  }

  public function setPassword($password)
  {
    if (!$password && 0 == strlen($password))
    {
      return;
    }

    if (!$salt = $this->getSalt())
    {
      $salt = md5(rand(100000, 999999).$this->getUsername());
      $this->setSalt($salt);
    }
    if (!$algorithm = $this->getAlgorithm())
    {
      $algorithm = sfConfig::get('app_sf_guard_plugin_algorithm_callable', 'sha1');
    }
    $algorithmAsStr = is_array($algorithm) ? $algorithm[0].'::'.$algorithm[1] : $algorithm;
    if (!is_callable($algorithm))
    {
      throw new sfException(sprintf('The algorithm callable "%s" is not callable.', $algorithmAsStr));
    }
    $this->setAlgorithm($algorithmAsStr);

    parent::_set('password', call_user_func_array($algorithm, array($salt.$password)));
  }

  public function setPasswordBis($password)
  {
  }

  public function checkPassword($password)
  {
    if ($callable = sfConfig::get('app_sf_guard_plugin_check_password_callable'))
    {
      return call_user_func_array($callable, array($this->getUsername(), $password, $this));
    }
    else
    {
      return $this->checkPasswordByGuard($password);
    }
  }

  public function checkPasswordByGuard($password)
  {
    $algorithm = $this->getAlgorithm();
    if (false !== $pos = strpos($algorithm, '::'))
    {
      $algorithm = array(substr($algorithm, 0, $pos), substr($algorithm, $pos + 2));
    }
    if (!is_callable($algorithm))
    {
      throw new sfException(sprintf('The algorithm callable "%s" is not callable.', $algorithm));
    }

    return $this->getPassword() == call_user_func_array($algorithm, array($this->getSalt().$password));
  }

  public function addGroupByName($name, $con = null)
  {
    $group = Doctrine::getTable('sfGuardGroup')->findOneByName($name);
    if (!$group)
    {
      throw new Exception(sprintf('The group "%s" does not exist.', $name));
    }

    $ug = new sfGuardUserGroup();
    $ug->setsfGuardUser($this);
    $ug->setsfGuardGroup($group);

    $ug->save($con);
  }

  public function addPermissionByName($name, $con = null)
  {
    $permission = Doctrine::getTable('sfGuardPermission')->findOneByName($name);
    if (!$permission)
    {
      throw new Exception(sprintf('The permission "%s" does not exist.', $name));
    }

    $up = new sfGuardUserPermission();
    $up->setsfGuardUser($this);
    $up->setsfGuardPermission($permission);

    $up->save($con);
  }

  public function hasGroup($name)
  {
    $this->loadGroupsAndPermissions();
    return isset($this->groups[$name]);
  }

  public function getGroupNames()
  {
    $this->loadGroupsAndPermissions();
    return array_keys($this->groups);
  }

  public function hasPermission($name)
  {
    $this->loadGroupsAndPermissions();
    return isset($this->permissions[$name]);
  }

  public function getPermissionNames()
  {
    $this->loadGroupsAndPermissions();
    return array_keys($this->permissions);
  }

  // merge of permission in a group + permissions
  public function getAllPermissions()
  {
    if (!$this->allPermissions)
    {
      $this->allPermissions = array();
      $permissions = $this->getPermissions();
      foreach ($permissions as $permission)
      {
        $this->allPermissions[$permission->getName()] = $permission;
      }

      foreach ($this->getGroups() as $group)
      {
        foreach ($group->getPermissions() as $permission)
        {
          $this->allPermissions[$permission->getName()] = $permission;
        }
      }
    }

    return $this->allPermissions;
  }

  public function getAllPermissionNames()
  {
    return array_keys($this->getAllPermissions());
  }

  public function loadGroupsAndPermissions()
  {
    $this->getAllPermissions();
    if (!$this->permissions)
    {
      $permissions = $this->getPermissions();
      foreach ($permissions as $permission)
      {
        $this->permissions[$permission->getName()] = $permission;
      }
    }
    if (!$this->groups)
    {
      $groups = $this->getGroups();
      foreach ($groups as $group)
      {
        $this->groups[$group->getName()] = $group;
      }
    }
  }
  public function reloadGroupsAndPermissions()
  {
    $this->groups         = null;
    $this->permissions    = null;
    $this->allPermissions = null;
  }

  public function delete(Doctrine_Connection $conn = null)
  {
    // delete profile if available
    try
    {
      if ($profile = $this->getProfile())
      {
        $profile->delete();
      }
    }
    catch (Exception $e)
    {
    }

    return parent::delete($conn);
  }

  public function setPasswordHash($v)
  {
    if (!is_null($v) && !is_string($v))
    {
      $v = (string) $v;
    }

    if ($this->password !== $v)
    {
      $this->password = $v;
    }
  }
}
