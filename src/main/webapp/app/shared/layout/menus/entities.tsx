import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';

import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown icon="th-list" name="Entities" id="entity-menu" data-cy="entity" style={{ maxHeight: '80vh', overflow: 'auto' }}>
    <MenuItem icon="asterisk" to="/periode">
      Periode
    </MenuItem>
    <MenuItem icon="asterisk" to="/category">
      Category
    </MenuItem>
    <MenuItem icon="asterisk" to="/history-line">
      History Line
    </MenuItem>
    <MenuItem icon="asterisk" to="/notification">
      Notification
    </MenuItem>
    <MenuItem icon="asterisk" to="/application-user">
      Application User
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
