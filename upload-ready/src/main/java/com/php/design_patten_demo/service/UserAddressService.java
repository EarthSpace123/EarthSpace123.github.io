package com.php.design_patten_demo.service;

import com.php.design_patten_demo.entity.UserAddress;
import com.php.design_patten_demo.repository.UserAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserAddressService {

    @Autowired
    private UserAddressRepository userAddressRepository;

    public List<UserAddress> getUserAddresses(Long userId) {
        return userAddressRepository.findByUserId(userId);
    }

    public UserAddress getAddressById(Long id) {
        return userAddressRepository.findById(id).orElse(null);
    }

    public UserAddress getDefaultAddress(Long userId) {
        return userAddressRepository.findByUserIdAndIsDefaultTrue(userId);
    }

    @Transactional
    public UserAddress createAddress(UserAddress address) {
        if (address.getIsDefault() != null && address.getIsDefault()) {
            userAddressRepository.findByUserId(address.getUserId()).forEach(addr -> {
                addr.setIsDefault(false);
                userAddressRepository.save(addr);
            });
        }
        return userAddressRepository.save(address);
    }

    @Transactional
    public UserAddress updateAddress(Long id, UserAddress addressDetails) {
        UserAddress address = userAddressRepository.findById(id).orElse(null);
        if (address == null) {
            return null;
        }

        address.setReceiverName(addressDetails.getReceiverName());
        address.setReceiverPhone(addressDetails.getReceiverPhone());
        address.setProvince(addressDetails.getProvince());
        address.setCity(addressDetails.getCity());
        address.setDistrict(addressDetails.getDistrict());
        address.setDetailAddress(addressDetails.getDetailAddress());
        address.setPostalCode(addressDetails.getPostalCode());

        if (addressDetails.getIsDefault() != null && addressDetails.getIsDefault()) {
            userAddressRepository.findByUserId(address.getUserId()).forEach(addr -> {
                addr.setIsDefault(false);
                userAddressRepository.save(addr);
            });
            address.setIsDefault(true);
        }

        return userAddressRepository.save(address);
    }

    @Transactional
    public void deleteAddress(Long id) {
        userAddressRepository.deleteById(id);
    }

    @Transactional
    public void setDefaultAddress(Long id, Long userId) {
        userAddressRepository.findByUserId(userId).forEach(addr -> {
            addr.setIsDefault(false);
            userAddressRepository.save(addr);
        });

        UserAddress address = userAddressRepository.findById(id).orElse(null);
        if (address != null && address.getUserId().equals(userId)) {
            address.setIsDefault(true);
            userAddressRepository.save(address);
        }
    }
}
